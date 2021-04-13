package com.shiroumi.shiroplayer.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.shiroumi.shiroplayer.Music

val processPostHandler = Handler(Looper.getMainLooper())
var processCallback: ((Float) -> Unit)? = null

val player: MediaPlayer by lazy {
    MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
        )
        setOnCompletionListener {
            processPostHandler.removeCallbacksAndMessages(null)
            stop()
        }
        setOnPreparedListener {
            start()
            updateProcess(currentPosition.toFloat() / duration)
        }
    }
}

fun Music.play(
    context: Context
) {
    if (player.isPlaying) return
    val musicUri = uri
    musicUri ?: return
    player.apply {
        player.reset()
        setDataSource(context, musicUri)
        prepare()
    }
}

fun Music.pause() {
    if (!player.isPlaying) return
    processPostHandler.removeCallbacksAndMessages(null)
    player.pause()
}

fun Music.resume() {
    if (player.isPlaying) return
    player.apply {
        updateProcess(currentPosition.toFloat() / duration)
        start()
    }
}

fun Music.stop() {
    if (!player.isPlaying) return
    processPostHandler.removeCallbacksAndMessages(null)
    player.stop()
}

fun MediaPlayer.updateProcess(
    process: Float
) {
    processPostHandler.postDelayed({
        processCallback?.invoke(process)
        updateProcess(currentPosition.toFloat() / duration)
    }, 100L)
}