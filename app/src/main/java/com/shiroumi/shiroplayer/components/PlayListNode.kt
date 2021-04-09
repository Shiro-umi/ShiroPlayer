package com.shiroumi.shiroplayer.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import com.shiroumi.shiroplayer.Music

class PlayListNode(music: Music) {
    var mediaPlayer: MediaPlayer? = null
    var music: Music? = null
        private set

    init {
        this.music = music
    }

    fun play(context: Context){
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
        }
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}