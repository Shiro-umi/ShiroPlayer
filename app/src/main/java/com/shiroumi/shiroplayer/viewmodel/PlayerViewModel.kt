package com.shiroumi.shiroplayer.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.IMusicServiceCommunication
import com.shiroumi.shiroplayer.MusicInfo
import com.shiroumi.shiroplayer.arch.viewmodel.BaseStatefulViewModel
import com.shiroumi.shiroplayer.components.PlayMode
import com.shiroumi.shiroplayer.room.entities.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NullSafeMutableLiveData")
class PlayerViewModel(
    savedStateHandle: SavedStateHandle
) : BaseStatefulViewModel(
    savedStateHandle
) {
    private var musicService: IMusicService? = null
    private var mainHandler = Handler(Looper.getMainLooper())
    private var seeking = false
    private var playerIsBusy = false

    val playList = MutableLiveData<MutableList<Music>>()

    private val observableMusic = MutableLiveData<Music>()
    var music: Music?
        get() = observableMusic.value
        set(value) {
            observableMusic.value = value
        }

    val observableCover = MutableLiveData<Bitmap>()
    var cover: Bitmap?
        get() = observableCover.value
        set(value) {
            observableCover.value = value
        }

    val observableIndex = MutableLiveData(Int.MIN_VALUE)
    var index: Int
        get() = observableIndex.value ?: Int.MIN_VALUE
        set(value) {
            observableIndex.value = value
        }

    val observableProgress = MutableLiveData(0f)
    var progress: Float
        get() = observableProgress.value ?: 0f
        set(value) {
            observableProgress.value = value
        }

    val observablePlayerState = MutableLiveData(PlayerState.STOP)
    var playerState: PlayerState
        get() = observablePlayerState.value ?: PlayerState.STOP
        set(value) {
            observablePlayerState.value = value
        }

    fun setBinder(musicService: IMusicService) {
        musicService.setCallback(callback)
        this.musicService = musicService
    }

    fun updateIndexContent() {
        launchBackground {
            mainHandler.post { this@PlayerViewModel.playList.value = playList ?: mutableListOf() }
        }
    }

    fun selectCurrentMusic() {
        launchBackground {
            mainHandler.post {
                currentMusicInfo.update()
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val play = {
        launchBackground {
            val musicInfo = play(index)
            mainHandler.post {
                musicInfo.update()
                playerState = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val playNext = {
        launchBackground {
            val musicInfo = playNext()
            mainHandler.post {
                musicInfo.update()
                playerState = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val playPrev = {
        launchBackground {
            val musicInfo = playPrev()
            mainHandler.post {
                musicInfo.update()
                playerState = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val pause = {
        launchBackground {
            pause()
            if (observablePlayerState.value == PlayerState.PAUSE) return@launchBackground
            mainHandler.post {
                playerState = PlayerState.PAUSE
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val resume = {
        launchBackground {
            resume()
            if (observablePlayerState.value == PlayerState.PLAYING) return@launchBackground
            mainHandler.post {
                playerState = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val stop = {
        launchBackground {
            stop()
            mainHandler.post {
                playerState = PlayerState.STOP
                resetProcess()
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    fun setPlayMode(playMode: PlayMode) {
        launchBackground { setPlayMode(playMode.value) }
    }

    fun localSeekTo(target: Float) {
        if (!seeking) seeking = true
        mainHandler.apply {
            removeCallbacks { }
            removeCallbacksAndMessages(null)
            observableProgress.value = target
            mainHandler.postDelayed({
                remoteSeekTo(target)
            }, 200L)
        }
    }

    private fun remoteSeekTo(target: Float) {
        val duration = music?.duration
        duration ?: return
        launchBackground { seekTo((duration * target).toLong()) }
    }

    fun clearCover() {
        cover = null
    }

    fun resetProcess() {
        progress = 0f
    }

    fun moveToNext() {
        moveToIndex(if (index == Int.MIN_VALUE) 0 else index + 1)
    }

    fun moveToPrev() {
        moveToIndex(if (index != Int.MIN_VALUE) index - 1 else index)
    }

    fun moveToIndex(index: Int) {
        val playList = playList.value ?: return
        when {
            (index >= playList.size) -> {
                this.index = 0
            }
            (index < 0) -> {
                this.index = playList.size - 1
            }
            else -> this.index = index
        }
    }

    fun refreshMusicDb() {
        playList.value = mutableListOf()
        launchBackground { refreshMusic() }
    }

    private fun MusicInfo?.update() {
        this ?: return
        observableMusic.value = music
        observableCover.value = cover
        observableIndex.value = index
    }

    fun (() -> Unit).withClickFilter(time: Long, before: (() -> Unit)? = {}) {
        before?.invoke()
        if (playerIsBusy) return
        with(mainHandler) {
            removeCallbacksAndMessages(null)
            postDelayed({
                this@withClickFilter()
                playerIsBusy = true
            }, time)
        }
    }

    private fun launchBackground(block: IMusicService.() -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                musicService?.run {
                    block(this)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mainHandler.removeCallbacksAndMessages(null)
    }

    private val callback = object : IMusicServiceCommunication.Stub() {
        override fun onMusicPlaying(process: Float) {
            if (seeking) return
            mainHandler.post { observableProgress.value = process }
        }

        override fun onSeekDone() {
            seeking = false
        }

        override fun onMusicChanged(musicInfo: MusicInfo?) {
            musicInfo.update()
        }

        override fun onMusicRefreshDone() {
            updateIndexContent()
        }
    }
}

enum class PlayerState {
    PLAYING,
    PAUSE,
    STOP
}