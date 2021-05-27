package com.shiroumi.shiroplayer.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long,
    @ColumnInfo(name = "name")
    val name: String = ""
)
