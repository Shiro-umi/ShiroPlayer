package com.shiroumi.shiroplayer.room.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import com.shiroumi.shiroplayer.room.entities.PlaylistMusicCrossRef

data class ByMusic(
    @Embedded val music: Music,
    @Relation(
        parentColumn = "musicId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistMusicCrossRef::class)
    )
    val playlistList: List<Playlist>
)
