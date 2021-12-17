package com.shiroumi.shiroplayer.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["name", "uri"])
data class PlaylistMusicCrossRef(
    @ColumnInfo(index = true) val name: String,
    @ColumnInfo(index = true) val uri: String
)
