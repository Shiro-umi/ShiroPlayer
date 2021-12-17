package com.shiroumi.shiroplayer.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import com.shiroumi.shiroplayer.IMusicServiceCommunication
import com.shiroumi.shiroplayer.IShiroService
import com.shiroumi.shiroplayer.IShiroServiceCallback
import com.shiroumi.shiroplayer.arch.service.BaseService
import com.shiroumi.shiroplayer.components.*
import com.shiroumi.shiroplayer.room.RoomManager
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist

lateinit var serviceCallback: IShiroServiceCallback

class ShiroService : BaseService() {
    val remoter by lazy { ShiroRemoter() }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        RoomManager.init(this)
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onUnbind(intent: Intent?): Boolean = super.onUnbind(intent)

    private val binder = object : IShiroService.Stub() {
        override fun addLibraryPath(path: String?): Boolean = path?.let { p ->
            return remoter.addLibraryPath(p)
        } ?: false

        override fun getLibraryPaths(): List<String> = remoter.getLibraryPath()

        override fun updateLibrary() = remoter.updateLibrary()

        override fun getPlaylists(): List<Playlist> = remoter.getPlaylists()

        override fun getPlaylist(name: String?): List<Music> = remoter.getPlaylist(name ?: "")

        override fun setPlayMode(playModeIndex: Int) {
            remoter.playMode = PlayMode.values()[playModeIndex]
        }

        override fun play(index: Int): Music? = remoter.play(index)

        override fun pause() = player.doPause()

        override fun resume() = player.doResume()

        override fun seekTo(position: Int) = player.doSeekTo(position)

        override fun stop() = player.doStop()

        override fun getCover(): Bitmap? = currentCover

        override fun getBlurryCover(): Bitmap? = currentBlurryCover

        override fun setCallback(callback: IShiroServiceCallback) {
            serviceCallback = callback
            completeCallback = {callback.onMusicChanged(remoter.playNext())}
        }
    }
}