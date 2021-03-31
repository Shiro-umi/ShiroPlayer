package com.shiroumi.shiroplayer.components

import android.content.ContentUris
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.provider.MediaStore
import com.shiroumi.shiroplayer.Music

class PlayListNode(
    private val music: Music
) : ListNode<PlayListNode>, Playable<PlayListNode> {
    private var mediaPlayer: MediaPlayer? = null
    private var next: PlayListNode? = null
    private var prev: PlayListNode? = null


    override fun prev(): PlayListNode? {
        return prev
    }

    override fun prev(listNode: PlayListNode?) {
        prev = listNode
    }

    override fun next(): PlayListNode? {
        return next
    }

    override fun next(listNode: PlayListNode?) {
        next = listNode
    }

    override fun play(context: Context): PlayListNode {
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

    fun getMusic(): Music {
        return music
    }

    override fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}