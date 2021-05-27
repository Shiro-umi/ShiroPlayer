package com.shiroumi.shiroplayer.components


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.liveData
import com.shiroumi.shiroplayer.room.RoomManager
import com.shiroumi.shiroplayer.room.dao.Dao
import com.shiroumi.shiroplayer.room.entities.Music
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep


class MusicSelector(
    private val contentResolver: ContentResolver
) {
    private val dao = RoomManager.db.dao()

    @SuppressLint("InlinedApi")
    private val normalProjection = arrayOf(
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.IS_MUSIC
    )

    fun updatePlayList(): List<Music> {
        // todo 后续增加按不同条件查询不同projection
        val projection = normalProjection

        return select(projection) { cursor ->
            buildNormalList(cursor)
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
            if (c.getInt(5) != 0) {
                val music = getMusic(c)
                playList.add(music)
            }
            c.moveToNext()
        }
        return playList
    }

    private fun getMusic(c: Cursor): Music {
        val music = Music()
        music.musicId = c.getLong(0)
        music.musicTitle = c.getString(1)
        music.artist = c.getString(2)
        music.album = c.getString(3)
        music.duration = c.getFloat(4)
        music.uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            c.getLong(0)
        ).toString()
        return music
    }

    fun getLocalMusicList(
        onLocalListEmpty: Dao.(List<Music>) -> Unit
    ): List<Music> {
        var musicList = dao.getMusicList()
        if (musicList.isEmpty()) {
            musicList = updatePlayList()
            dao.onLocalListEmpty(musicList)
        }
        return musicList
    }

    fun refreshMusic(onRefreshDone: List<Music>.() -> Unit) {
        with(updatePlayList()) {
            dao.addAllMusic(this)
            sleep(2000L)
            onRefreshDone()
        }
    }
}