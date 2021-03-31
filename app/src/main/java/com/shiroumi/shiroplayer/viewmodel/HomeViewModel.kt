package com.shiroumi.shiroplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.Music

class HomeViewModel : ViewModel() {

    private var musicService: IMusicService? = null

    val music: MutableLiveData<Music> = MutableLiveData()

    fun setBinder(musicService: IMusicService?) {
        this.musicService = musicService
    }

    fun getBinder(): IMusicService? {
        return musicService
    }

    fun play() {
        musicService?.play()?.apply {
            music.value = this
        }
    }

    fun playNext() {
        musicService?.playNext()?.apply {
            music.value = this
        }
    }
}