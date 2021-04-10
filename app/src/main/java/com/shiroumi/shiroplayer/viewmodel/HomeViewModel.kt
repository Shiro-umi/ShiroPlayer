package com.shiroumi.shiroplayer.viewmodel

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiroumi.shiroplayer.IMusicSercviceCommunication
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var musicService: IMusicService? = null

    val playList = MutableLiveData<MutableList<Music>>()
    val music = MutableLiveData<Music>()
    val musicCover = MutableLiveData<Bitmap?>()
    val playingProcess = MutableLiveData(0f)

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

    fun play(index: Int = -1) {
        viewModelScope.launch {
            musicCover.value = null
            val res = remotePlay(index)
            music.value = res.first
            musicCover.value = res.second
        }
    }

    private suspend fun remotePlay(index: Int = -1): Pair<Music?, Bitmap?> {
        return withContext(Dispatchers.IO) {
            var currentMusic: Music? = null
            var currentCover: Bitmap? = null
            musicService?.apply {
                play(index)?.let { music ->
                    currentMusic = music
                }
                musicCover.let { cover ->
                    currentCover = cover
                }
            }
            currentMusic to currentCover
        }
    }

    fun playNext() {
        viewModelScope.launch {
            musicService?.playNext()?.apply {
                music.value = this
            }
        }
    }

    private val callback = object : IMusicSercviceCommunication.Stub() {
        override fun onMusicPlaying(process: Float) {
            Handler(Looper.getMainLooper()).post { playingProcess.value = process }
        }
    }
}