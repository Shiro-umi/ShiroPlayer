package com.shiroumi.shiroplayer.components

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.shiroumi.shiroplayer.Music

class MusicSelector(private val contentResolver: ContentResolver) {

    fun updatePlayList(): PlayListNode? {
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION
            ),
            null,
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
        cursor ?: return null
        var head: PlayListNode? = null
        var pointer = head

        cursor.apply {
            moveToFirst()
            while (position < count) {
                val music = getMusic(this)
                val nextNode = PlayListNode(music)
                if (head == null) {
                    head = nextNode
                    pointer = head
                    moveToNext()
                    continue
                }
                pointer?.next(nextNode)
                nextNode.prev(pointer)
                pointer = nextNode
                moveToNext()
            }
            close()
        }
        pointer?.next(head)
        head?.prev(pointer)

        return head
    }

    private fun getMusic(cursor: Cursor): Music {
        val music = Music()
        music._id = cursor.getLong(0)
        music.title = cursor.getString(1)
        music.artist = cursor.getString(2)
        music.album = cursor.getString(3)
        music.duration = cursor.getLong(4)
        return music
    }
}