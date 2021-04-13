package com.shiroumi.shiroplayer.viewmodel

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiroumi.shiroplayer.IMusicServiceCommunication
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.components.player
import com.shiroumi.shiroplayer.components.processCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var musicService: IMusicService? = null
    private var mainHandler = Handler(Looper.getMainLooper())

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

    fun play(index: Int = -1) {
        musicCover.value = null
        mainHandler.removeCallbacksAndMessages(null)
        mainHandler.postDelayed({
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
        }, 200L)
    }

    fun pause() {
        if (musicState == MusicState.PAUSE) return
        musicState = MusicState.PAUSE
        launchInIOThread { service ->
            service.pause()
        }
    }

    fun resume() {
        if (musicState == MusicState.PLAYING) return
        musicState = MusicState.PLAYING
        launchInIOThread { service ->
            service.resume()
        }
    }

    fun playNext() {
        viewModelScope.launch {
            musicService?.playNext()?.apply {
                music.value = this
            }
        }
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

    override fun onCleared() {
        super.onCleared()
        mainHandler.removeCallbacksAndMessages(null)
    }

    private val callback = object : IMusicServiceCommunication.Stub() {
        override fun onMusicPlaying(process: Float) {
            mainHandler.post { playingProcess.value = process }
        }
    }
}