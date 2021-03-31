package com.shiroumi.shiroplayer.service

import android.content.Intent
import android.os.IBinder
import androidx.compose.ui.ExperimentalComposeUiApi
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.arch.service.BaseService
import com.shiroumi.shiroplayer.components.Remoter

class MusicService : BaseService() {
    lateinit var remoter: Remoter

    @ExperimentalComposeUiApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        remoter = Remoter(this, contentResolver)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return token
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    private val token = object : IMusicService.Stub() {
        override fun play(): Music? {
            remoter.play()
            return remoter.current()
        }

        override fun playNext(): Music? {
            remoter.playNext()
            return remoter.current()
        }

        override fun getCurrentMusic(): Music? {
            return remoter.current()
        }
    }
}