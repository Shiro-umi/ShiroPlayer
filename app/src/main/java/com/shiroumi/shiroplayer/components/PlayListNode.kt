package com.shiroumi.shiroplayer.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.shiroumi.shiroplayer.IMusicSercviceCommunication
import com.shiroumi.shiroplayer.Music

class PlayListNode(music: Music) {
    var mediaPlayer: MediaPlayer? = null
    var music: Music? = null
        private set
    var processCallback: IMusicSercviceCommunication? = null
    var postProcessHandler: Handler? = null

    init {
        this.music = music
    }

    fun play(context: Context) {
        val music = this.music
        val uri = music?.uri
        music ?: return
        uri ?: return
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
            )
            setDataSource(context, uri)
            prepare()
            start()
            postProcessHandler = Handler(Looper.getMainLooper())
            postProcessHandler?.startUpdateProcess(currentPosition, music)
        }
    }

    fun stop() {
        postProcessHandler?.removeCallbacksAndMessages(null)
        postProcessHandler = null
        processCallback = null
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun Handler.startUpdateProcess(currentPosition: Int, music: Music) {
        postDelayed({
            val process = currentPosition / music.duration
            if (process >= 0.99f) {
                stop()
                return@postDelayed
            }
            processCallback?.onMusicPlaying(process)
            startUpdateProcess(mediaPlayer?.currentPosition ?: 0, music)
        }, 10L)
    }
}