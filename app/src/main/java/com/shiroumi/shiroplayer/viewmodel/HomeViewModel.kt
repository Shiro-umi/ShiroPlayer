package com.shiroumi.shiroplayer.viewmodel

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    savedStateHandle: SavedStateHandle
) : BaseStatefulViewModel(
    savedStateHandle
) {
    private var musicService: IMusicService? = null
    private var mainHandler = Handler(Looper.getMainLooper())
    private var seeking = false

    val playList = MutableLiveData<MutableList<Music>>()
    val music = MutableLiveData<Music>()
    val musicCover = MutableLiveData<Bitmap?>()
    val musicIndex = MutableLiveData(-1)
    val playingProcess = MutableLiveData(0f)
    var musicState: MusicState = MusicState.STOP
        private set

    enum class MusicState {
        PLAYING,
        PAUSE,
        STOP
    }

    fun setBinder(musicService: IMusicService) {
        musicService.setCallback(callback)
        this.musicService = musicService
    }

    fun updateIndexContent() {
        viewModelScope.launch {
            musicService?.playList?.apply {
                playList.value = this
            }
        }
    }

    fun selectCurrentMusic() {
        launchInIOThread { service ->
            val currentMusic = service.currentMusic
            val currentCover = service.musicCover
            val currentIndex = service.currentIndex
            mainHandler.post {
                music.value = currentMusic
                musicCover.value = currentCover
                musicIndex.value = currentIndex
            }
        }
    }

    val play: (Int) -> Unit = { index ->
        musicCover.value = null
        launchInIOThread { service ->
            var currentMusic: Music?
            var currentCover: Bitmap?
            service.play(index).let { music ->
                currentMusic = music
            }
            service.musicCover.let { cover ->
                currentCover = cover
            }
            mainHandler.post {
                music.value = currentMusic
                musicCover.value = currentCover
                musicState = MusicState.PLAYING
            }
        }
    }

    val pause = {
        launchInIOThread { service ->
            service.pause()
            if (musicState == MusicState.PAUSE) return@launchInIOThread
            musicState = MusicState.PAUSE
        }
    }

    val resume = {
        launchInIOThread { service ->
            service.resume()
            if (musicState == MusicState.PLAYING) return@launchInIOThread
            musicState = MusicState.PLAYING
        }
    }

    fun playNext() {
        viewModelScope.launch {
            musicService?.playNext()?.apply {
                music.value = this
            }
        }
    }

    fun clearCoverNow() {
        musicCover.value = null
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
        launchInIOThread { service ->
            service.seekTo((duration * target).toLong())
        }
    }

    fun resetProcessNow() {
        playingProcess.value = 0f
    }

    private fun launchInIOThread(block: (IMusicService) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                musicService?.apply {
                    block(this)
                }
            }
        }
    }

    fun (() -> Unit).withClickFilter(time: Long) {
        mainHandler.removeCallbacksAndMessages(null)
        mainHandler.postDelayed(this, time)
    }

    fun ((Int) -> Unit).withClickFilter(index: Int, time: Long, before: (() -> Unit)? = {}) {
        before?.invoke()
        mainHandler.removeCallbacksAndMessages(null)
        mainHandler.postDelayed({
            this(index)
        }, time)
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
    }
}