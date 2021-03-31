package com.shiroumi.shiroplayer.components

import android.content.ContentUris
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.provider.MediaStore
import com.shiroumi.shiroplayer.Music

class PlayListNode(music: Music) {
    var mediaPlayer: MediaPlayer? = null
    var next: PlayListNode? = null
    var prev: PlayListNode? = null
    var music: Music? = null
        private set

    init {
        this.music = music
    }

    fun play(context: Context): PlayListNode? {
        val music = this.music
        music ?: return null
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
            )
            setDataSource(
                context,
                ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    music._id
                )
            )
            prepare()
            start()
        }
        return this
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}