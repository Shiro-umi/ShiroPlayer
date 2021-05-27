package com.shiroumi.shiroplayer.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shiroumi.shiroplayer.room.dao.Dao
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import com.shiroumi.shiroplayer.room.entities.PlaylistMusicCrossRef

@Database(
    entities = [
        Music::class,
        Playlist::class,
        PlaylistMusicCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}