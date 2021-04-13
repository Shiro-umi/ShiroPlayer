package com.shiroumi.shiroplayer.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import com.shiroumi.shiroplayer.IMusicServiceCommunication
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.arch.service.BaseService
import com.shiroumi.shiroplayer.components.Remoter
import com.shiroumi.shiroplayer.components.processCallback

class MusicService : BaseService() {
    lateinit var remoter: Remoter

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        remoter = Remoter(this, contentResolver)
    }

    override fun onBind(intent: Intent?): IBinder {
        return token
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    private val token = object : IMusicService.Stub() {
        override fun play(int: Int): Music? {
            remoter.play(int)
            return remoter.currentMusic
        }

        override fun playNext(): Music? {
            remoter.playNext()
            return remoter.currentMusic
        }

        override fun pause() {
            remoter.pause()
        }

        override fun resume() {
            remoter.resume()
        }

        override fun seekTo(position: Long) {
        }

        override fun getCurrentMusic(): Music? {
            return remoter.currentMusic
        }

        override fun getCurrentIndex(): Int {
            return remoter.currentIndex
        }

        override fun getPlayList(): List<Music>? {
            return remoter.playList
        }

        override fun getMusicCover(): Bitmap? {
            return remoter.currentMusicCover
        }

        override fun setCallback(callback: IMusicServiceCommunication?) {
            processCallback = { process -> callback?.onMusicPlaying(process) }
        }
    }
}