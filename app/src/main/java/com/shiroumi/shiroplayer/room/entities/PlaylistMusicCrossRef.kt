package com.shiroumi.shiroplayer.room.entities

import androidx.room.Entity

@Entity(
    tableName = "playlist_music_cross_ref",
    primaryKeys = ["playlistId", "musicId"]
)
data class PlaylistMusicCrossRef(
    val playlistId: Long,
    val musicId: Long
)
