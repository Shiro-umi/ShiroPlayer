package com.shiroumi.shiroplayer.components

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.shiroumi.shiroplayer.arch.application
import com.shiroumi.shiroplayer.composable.common.toFile
import com.shiroumi.shiroplayer.service.serviceCallback
import java.io.File

val processPostHandler = Handler(Looper.getMainLooper())

var completeCallback: (() -> Unit)? = null

var mediaFile: File? = null

val MediaPlayer.process: Float
    get() = currentPosition.toFloat() / duration

val player: MediaPlayer by lazy {
    MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build()
        )
        setOnPreparedListener {
            start()
            updateProcess()
        }
        setOnCompletionListener {
            completeCallback?.invoke()
        }
    }
}

fun Uri.play() = with(player) {
    try {
        if (isPlaying) return
        reset()
        mediaFile = this@play.toFile(application)
        mediaFile?.let {
            processPostHandler.removeCallbacksAndMessages(null)
            setDataSource(it.absolutePath)
            prepare()
        } ?: serviceCallback.onUriResourceUnavilable()
    } catch (e: Exception) {
        serviceCallback.onUriResourceUnavilable()
    }
}

fun MediaPlayer.doPause() {
    if (!isPlaying) return
    processPostHandler.removeCallbacksAndMessages(null)
    pause()
}

fun MediaPlayer.doResume() {
    if (isPlaying) return
    updateProcess()
    start()
}

fun MediaPlayer.doStop() {
    if (!isPlaying) return
    processPostHandler.removeCallbacksAndMessages(null)
    mediaFile?.delete()
    mediaFile = null
    stop()
}

fun MediaPlayer.doSeekTo(target: Int) {
    this.duration
    seekTo(target)
    serviceCallback.onSeekDone()
}

fun MediaPlayer.updateProcess() {
    processPostHandler.postDelayed({
        serviceCallback.onMusicPlaying(process)
        updateProcess()
    }, 500L)
}
