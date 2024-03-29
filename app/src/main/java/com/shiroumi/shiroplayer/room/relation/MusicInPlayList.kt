package com.shiroumi.shiroplayer.room.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import com.shiroumi.shiroplayer.room.entities.PlaylistMusicCrossRef

data class MusicInPlayList(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "name",
        entityColumn = "uri",
        associateBy = Junction(PlaylistMusicCrossRef::class)
    )
    val musicList: List<Music>
)
