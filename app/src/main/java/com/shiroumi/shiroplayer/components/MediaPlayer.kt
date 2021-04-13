package com.shiroumi.shiroplayer.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.shiroumi.shiroplayer.IMusicSercviceCommunication
import com.shiroumi.shiroplayer.Music

val processPostHandler = Handler(Looper.getMainLooper())

val player: MediaPlayer by lazy {
    MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
        )
    }
}

fun Music.play(
    context: Context,
    processCallback: IMusicSercviceCommunication?
) {
    val musicUri = uri
    musicUri ?: return
    player.apply {
        setDataSource(context, musicUri)
        setOnCompletionListener {
            processPostHandler.removeCallbacksAndMessages(null)
            stop()
        }
        setOnPreparedListener {
            start()
            updateProcess(currentPosition.toFloat() / duration, processCallback)
        }
        prepare()
    }
}

fun Music.stop() {
    if (player.isPlaying) {
        processPostHandler.removeCallbacksAndMessages(null)
        player.stop()
        player.reset()
    }
}

fun MediaPlayer.updateProcess(
    process: Float,
    processCallback: IMusicSercviceCommunication?
) {
    processPostHandler.postDelayed({
        processCallback?.onMusicPlaying(process)
        updateProcess(currentPosition.toFloat() / duration, processCallback)
    }, 100L)
}