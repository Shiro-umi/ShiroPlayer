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
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.arch.viewmodel.BaseStatefulViewModel
import com.shiroumi.shiroplayer.components.PlayMode
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
    val music = MutableLiveData<Music>()
    val musicCover = MutableLiveData<Bitmap>()
    val musicIndex = MutableLiveData(-1)
    val playingProcess = MutableLiveData(0f)
    var playerState: MutableLiveData<PlayerState> = MutableLiveData(PlayerState.STOP)
        private set
    var currentIndex: Int = -1
        private set
        get() = musicIndex.value ?: field

    fun setBinder(musicService: IMusicService) {
        musicService.setCallback(callback)
        this.musicService = musicService
    }

    fun updateIndexContent() {
        launchBackground { service ->
            mainHandler.post { playList.value = service.playList ?: mutableListOf() }
        }
    }

    fun selectCurrentMusic() {
        launchBackground { service ->
            val currentMusic = service.currentMusic
            val currentCover = service.musicCover
            val currentIndex = service.currentIndex
            mainHandler.post {
                music.value = currentMusic
                musicCover.value = currentCover
                musicIndex.value = currentIndex
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val play = {
        launchBackground { service ->
            var currentMusic: Music?
            var currentCover: Bitmap?
            with(service.play(currentIndex)) {
                currentMusic = this
            }
            with(service.musicCover) {
                currentCover = this
            }
            mainHandler.post {
                music.value = currentMusic
                musicCover.value = currentCover
                playerState.value = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val pause = {
        launchBackground { service ->
            service.pause()
            if (playerState.value == PlayerState.PAUSE) return@launchBackground
            mainHandler.post {
                playerState.value = PlayerState.PAUSE
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val resume = {
        launchBackground { service ->
            service.resume()
            if (playerState.value == PlayerState.PLAYING) return@launchBackground
            mainHandler.post {
                playerState.value = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val playNext = {
        launchBackground { service ->
            var currentMusic: Music?
            var currentCover: Bitmap?
            with(service.playNext()) {
                currentMusic = this
            }
            with(service.musicCover) {
                currentCover = this
            }
            mainHandler.post {
                music.value = currentMusic
                musicCover.value = currentCover
                playerState.value = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }

    val playPrev = {
        launchBackground { service ->
            var currentMusic: Music?
            var currentCover: Bitmap?
            with(service.playPrev()) {
                currentMusic = this
            }
            with(service.musicCover) {
                currentCover = this
            }
            mainHandler.post {
                music.value = currentMusic
                musicCover.value = currentCover
                playerState.value = PlayerState.PLAYING
                if (playerIsBusy) playerIsBusy = false
            }
        }
    }
    val stop = {
        launchBackground { service ->
            service.stop()
        }
    }

    fun setPlayMode(playMode: PlayMode) {
        launchBackground { service ->
            service.setPlayMode(playMode.value)
        }
    }

    fun localSeekTo(target: Float) {
        if (!seeking) seeking = true
        mainHandler.apply {
            removeCallbacksAndMessages(null)
            playingProcess.value = target
            mainHandler.postDelayed({
                remoteSeekTo(target)
            }, 200L)
        }
    }

    private fun remoteSeekTo(target: Float) {
        val duration = music.value?.duration
        duration ?: return
        launchBackground { service ->
            service.seekTo((duration * target).toLong())
        }
    }

    fun clearCover() {
        musicCover.value = null
    }

    fun resetProcess() {
        playingProcess.value = 0f
    }

    fun resetIndex() {
        musicIndex.value = -1
    }

    val moveToNext = moveToIndex(currentIndex + 1)

    val moveToPrev = moveToIndex(currentIndex - 1)

    fun moveToIndex(index: Int) {
        val playList = playList.value ?: return
        if (index < 0) {
            musicIndex.value = playList.size - 1
        } else {
            musicIndex.value = index
        }
    }

    private fun launchBackground(block: (IMusicService) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                musicService?.let { service ->
                    block(service)
                }
            }
        }
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

    override fun onCleared() {
        super.onCleared()
        mainHandler.removeCallbacksAndMessages(null)
    }

    private val callback = object : IMusicServiceCommunication.Stub() {
        override fun onMusicPlaying(process: Float) {
            if (seeking) return
            mainHandler.post { playingProcess.value = process }
        }

        override fun onSeekDone() {
            seeking = false
        }

        override fun onMusicChanged() {
            selectCurrentMusic()
        }
    }
}

enum class PlayerState {
    PLAYING,
    PAUSE,
    STOP
}