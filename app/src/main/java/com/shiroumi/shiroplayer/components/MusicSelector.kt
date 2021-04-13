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

    fun updatePlayList(playMode: PlayMode): List<Music> {
        // todo 后续增加按不同条件查询不同projection
        val projection = normalProjection

        return when (playMode) {
            PlayMode.NORMAL -> select(projection) { cursor ->
                buildNormalList(cursor)
            }
            PlayMode.SINGLE -> select(projection) { cursor ->
                buildNormalList(cursor)
            }
            PlayMode.RANDOM -> select(projection) { cursor ->
                buildNormalList(cursor)
            }
        }
    }

    private fun select(
        projection: Array<String>,
        block: (Cursor) -> List<Music>
    ): List<Music> {
        val result = mutableListOf<Music>()
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )?.apply {
            result.addAll(block(this))
            close()
        }
        return result
    }

    private fun buildNormalList(c: Cursor): List<Music> {
        val playList = mutableListOf<Music>()
        c.moveToFirst()
        while (c.position < c.count) {
            val music = getMusic(c)
            playList.add(music)
            c.moveToNext()
        }
        return playList
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