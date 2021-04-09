package com.shiroumi.shiroplayer.components


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore
import com.shiroumi.shiroplayer.Music


class MusicSelector(
    private val contentResolver: ContentResolver
) {
    @SuppressLint("InlinedApi")
    private val normalProjection = arrayOf(
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION
    )

    fun updatePlayList(@PlayMode playMode: String): PlayListNode? {
        var head: PlayListNode? = null
        // todo 后续增加按不同条件查询不同projection
        val projection = normalProjection

        when (playMode) {
            PLAY_MODE_NORMAL -> select(projection) { cursor ->
                head = buildNormalList(cursor)
            }

            PLAY_MODE_RANDOM -> select(projection) { cursor ->
                head = buildNormalList(cursor)
            }

            PLAY_MODE_SINGLE -> select(projection) { cursor ->
                head = buildNormalList(cursor)
            }
        }
        return head
    }

    private fun select(
        projection: Array<String>,
        block: (Cursor) -> Unit
    ) {
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )?.apply {
            block(this)
            close()
        }
    }

    private fun buildNormalList(c: Cursor): PlayListNode? {
        var head: PlayListNode? = null
        var pointer = head
        c.moveToFirst()
        while (c.position < c.count) {
            val music = getMusic(c)
            val nextNode = PlayListNode(music)
            if (head == null) {
                head = nextNode
                pointer = head
                c.moveToNext()
                continue
            }
            pointer?.next = nextNode
            nextNode.prev = pointer
            pointer = nextNode
            c.moveToNext()
        }
        pointer?.next = head
        head?.prev = pointer
        return head
    }

    private fun getMusic(c: Cursor): Music {
        val music = Music()
        music._id = c.getLong(0)
        music.title = c.getString(1)
        music.artist = c.getString(2)
        music.album = c.getString(3)
        music.duration = c.getLong(4)
        music.uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            c.getLong(0)
        )
//        val retriever = MediaMetadataRetriever()
//        retriever.setDataSource(context, music.uri)
//        val pic = retriever.embeddedPicture
//        retriever.release()
        return music
    }
}