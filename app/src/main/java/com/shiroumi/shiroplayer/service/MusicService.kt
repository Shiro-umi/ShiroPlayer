package com.shiroumi.shiroplayer.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.IMusicServiceCommunication
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.MusicInfo
import com.shiroumi.shiroplayer.arch.service.BaseService
import com.shiroumi.shiroplayer.components.*

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
        override fun play(int: Int): MusicInfo? {
            return remoter.play(int)
        }

        override fun playNext(): MusicInfo? {
            return remoter.playNext()
        }

        override fun playPrev(): MusicInfo? {
            return remoter.playPrev()
        }

        override fun getCurrentMusicInfo(): MusicInfo? {
            return remoter.currentMusicInfo
        }

        override fun pause() {
            remoter.pause()
        }

        override fun resume() {
            remoter.resume()
        }

        override fun seekTo(target: Long) {
            remoter.seekTo(target)
        }

        override fun stop() {
            remoter.stop()
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
            callback?.apply {
                processCallback = { process -> onMusicPlaying(process) }
                seekCallback = { onSeekDone() }
                changeMusicCallback = { musicInfo -> onMusicChanged(musicInfo) }
            }
        }

        override fun setPlayMode(playMode: Int) {
            remoter.playMode = playMode
        }
    }
}