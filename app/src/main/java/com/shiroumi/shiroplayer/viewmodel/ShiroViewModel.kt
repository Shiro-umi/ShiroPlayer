package com.shiroumi.shiroplayer.viewmodel

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiroumi.shiroplayer.IShiroService
import com.shiroumi.shiroplayer.IShiroServiceCallback
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShiroViewModel : ViewModel() {

    // Service
    private lateinit var service: IShiroService

    // Playlist
    var mPlaylist: MutableLiveData<List<Music>?> = MutableLiveData(null)

    // Playlists
    var mPlayLists: MutableLiveData<List<Playlist>> = MutableLiveData(emptyList())

    // Paths
    var mLibraryPaths: MutableLiveData<List<String>> = MutableLiveData(null)

    // LibraryPathState
    var mLibraryState: MutableLiveData<LibraryState> = MutableLiveData(LibraryState.Default)

    // CurrentIndex
    var currentIndex: MutableLiveData<Int> = MutableLiveData<Int>(Int.MIN_VALUE)

    // CurrentPlaylist
    var currentPlaylist: MutableLiveData<String> = MutableLiveData(null)

    var currentCover: MutableLiveData<Bitmap?> = MutableLiveData(null)

    // PlayerState
    val currentPlayerState: MutableLiveData<PlayerState> = MutableLiveData(PlayerState.Stop)

    val progress: MutableLiveData<Float> = MutableLiveData(0f)

    val currentMusic: Music?
        get() {
            val index = currentIndex.value ?: return null
            return mPlaylist.value?.get(index)
        }

    var pendingSeek = false

    /**
     * 设置播放模式
     * @param playModeOrdinal 播放模式对应索引
     */
    fun setPlayMode(playModeOrdinal: Int) = interProcess {
        setPlayMode(playModeOrdinal)
    }

    /**
     * 查询媒体库path
     */
    private fun getLibraryPath() {
        if (mLibraryState.value !is LibraryState.Updating) {
            mLibraryState.postValue(LibraryState.Updating)
        }
        interProcess {
            val libraryPaths = libraryPaths
            mLibraryPaths.postValue(libraryPaths)
            mLibraryState.postValue(
                if (libraryPaths.isNotEmpty()) LibraryState.NotEmpty else LibraryState.Empty
            )
        }
    }

    /**
     * 添加媒体库path
     */
    fun addLibraryPath(path: String) {
        mLibraryState.postValue(LibraryState.Updating)
        interProcess {
            if (addLibraryPath(path)) {
                getLibraryPath()
                refreshPlaylists()
            }
        }
    }

    /**
     * 获取所有播放列表
     */
    fun refreshPlaylists() = interProcess {
        val playlists = playlists.apply {
            add(Playlist(name = "Default"))
        }
        currentPlaylist.postValue(playlists.first().name)
        refreshPlaylist(playlists.first().name)
    }

    /**
     * 获取特定Playlist内容
     */
    fun refreshPlaylist(name: String) = interProcess {
        mPlaylist.postValue(getPlaylist(name))
    }

    /**
     * 刷新当前playlist
     */
    fun refreshCurrentPlaylist() = interProcess {
        mPlaylist.postValue(getPlaylist(currentPlaylist.value))
    }

    private fun resetCurrent() {
        currentCover.postValue(null)
        progress.postValue(0f)
    }

    /**
     * 播放
     */
    fun play(index: Int) {
        if (currentIndex.value != index) {
            currentIndex.postValue(index)
            resetCurrent()
        }
        currentPlayerState.postValue(PlayerState.Playing)
        interProcess {
            stop()
            play(index)
            currentCover.postValue(blurryCover)
        }
    }

    fun pause() {
        currentPlayerState.postValue(PlayerState.Pause)
        interProcess {
            pause()
        }
    }

    fun resume() {
        currentPlayerState.postValue(PlayerState.Playing)
        interProcess {
            resume()
        }
    }

    fun stop() {
        currentPlayerState.postValue(PlayerState.Stop)
        interProcess {
            stop()
        }
    }

    fun playPrev() {
        val curr = currentIndex.value ?: return
        val next = if (curr == 0) {
            mPlaylist.value?.size?.minus(1) ?: 0
        } else {
            curr - 1
        }
        currentIndex.postValue(next)
        currentPlayerState.postValue(PlayerState.Playing)
        play(next)
    }

    fun playNext() {
        val curr = currentIndex.value ?: return
        val next = if (curr == mPlaylist.value?.size?.minus(1)) {
            0
        } else {
            curr + 1
        }
        currentIndex.postValue(next)
        currentPlayerState.postValue(PlayerState.Playing)
        play(next)
    }

    fun seekRemote() = interProcess {
        seekTo(((progress.value ?: 0f) * (currentMusic?.duration ?: 0f)).toInt())
    }

    fun seekLocal(progress: Float) = this.progress.postValue(progress)

    fun getCover() = interProcess {
        currentCover.postValue(cover)
    }

    fun getBlurryCover() = interProcess {
        currentCover.postValue(blurryCover)
    }

    private val callback = object : IShiroServiceCallback.Stub() {

        override fun onMusicPlaying(process: Float): Unit =
            if (!pendingSeek) progress.postValue(process) else Unit

        override fun onSeekDone() {
            pendingSeek = false
        }

        override fun onMusicChanged(index: Int) {
            resetCurrent()
            currentIndex.postValue(index)
            currentCover.postValue(service.blurryCover)
        }

        override fun onLibraryUpdated() {
        }

        override fun onMusicRefreshDone() {
        }

        override fun onUriResourceUnavilable() {
        }

        override fun onMusicDeleted() {
        }

    }

    // 设置Service
    fun setBinder(token: IShiroService) {
        service = token
        service.setCallback(callback)
        getLibraryPath()
    }

    // 跨进程通信
    private fun interProcess(work: IShiroService.() -> Unit) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            service.work()
        }
    }
}

sealed class PlayerState {
    object Playing : PlayerState()
    object Pause : PlayerState()
    object Stop : PlayerState()
}

sealed class LibraryState {
    object Default : LibraryState()
    object Empty : LibraryState()
    object NotEmpty : LibraryState()
    object Updating : LibraryState()
}
