package com.shiroumi.shiroplayer

import android.content.Context
import com.shiroumi.shiroplayer.Song
import android.provider.MediaStore
import android.util.Log
import java.util.ArrayList

object Scanner {
    fun getMusicData(context: Context): List<Song> {
        val list: MutableList<Song> = ArrayList()
        // 媒体库查询语句（写一个工具类MusicUtils）
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
            null, MediaStore.Audio.AudioColumns.IS_MUSIC
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val song = Song()
//                song.song =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)) //歌曲名称
//                song.singer =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)) //歌手
//                song.album =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)) //专辑名
//                song.path =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) //歌曲路径
//                song.duration =
//                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) //歌曲时长
//                song.size =
//                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)) //歌曲大小
                if (song.size > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (song.song.contains("-")) {
                        val str = song.song.split("-").toTypedArray()
                        song.singer = str[0]
                        song.song = str[1]
                    }
                    list.add(song)
                }
            }
            // 释放资源
            cursor.close()
        }
        return list
    }

    //专辑图片
    private fun imgUrl(context: Context): String? {
        var album_art: String? = null
        val mediaColumns1 =
            arrayOf(MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ALBUM)
        val cursor1 = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, mediaColumns1, null, null,
            null
        )
        if (cursor1 != null) {
            cursor1.moveToFirst()
            do {
                album_art = cursor1.getString(0)
                if (album_art != null) {
                    Log.d("ALBUM_ART", album_art)
                }
                val album = cursor1.getString(1)
                if (album != null) {
                    Log.d("ALBUM_ART", album)
                }
            } while (cursor1.moveToNext())
            cursor1.close()
        }
        return album_art
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    fun formatTime(time: Int): String {
        return if (time / 1000 % 60 < 10) {
            (time / 1000 / 60).toString() + ":0" + time / 1000 % 60
        } else {
            (time / 1000 / 60).toString() + ":" + time / 1000 % 60
        }
    }
}