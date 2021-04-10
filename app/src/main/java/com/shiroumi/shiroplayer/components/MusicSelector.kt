package com.shiroumi.shiroplayer.components


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
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

    fun updatePlayList(@PlayMode playMode: String): Pair<List<PlayListNode?>, List<Music>>? {
        // todo 后续增加按不同条件查询不同projection
        val projection = normalProjection

        return when (playMode) {
            PLAY_MODE_NORMAL -> select(projection) { cursor ->
                buildNormalList(cursor)
            }

            PLAY_MODE_RANDOM -> select(projection) { cursor ->
                buildNormalList(cursor)
            }

            PLAY_MODE_SINGLE -> select(projection) { cursor ->
                buildNormalList(cursor)
            }
            else -> null
        }
    }

    private fun select(
        projection: Array<String>,
        block: (Cursor) -> Pair<List<PlayListNode?>, List<Music>>
    ): Pair<List<PlayListNode?>, List<Music>>? {
        var result: Pair<List<PlayListNode?>, List<Music>>? = null
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )?.apply {
            result = block(this)
            close()
        }
        return result
    }

    private fun buildNormalList(c: Cursor): Pair<List<PlayListNode?>, List<Music>> {
        val realPlayList = mutableListOf<PlayListNode?>()
        val playList = mutableListOf<Music>()
        c.moveToFirst()
        while (c.position < c.count) {
            val music = getMusic(c)
            realPlayList.add(PlayListNode(music))
            playList.add(music)
            c.moveToNext()
        }
        return realPlayList to playList
    }

    private fun getMusic(c: Cursor): Music {
        val music = Music()
        music._id = c.getLong(0)
        music.title = c.getString(1)
        music.artist = c.getString(2)
        music.album = c.getString(3)
        music.duration = c.getFloat(4)
        music.uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            c.getLong(0)
        )
        return music
    }
}