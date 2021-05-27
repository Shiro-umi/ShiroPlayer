package com.shiroumi.shiroplayer.room.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import com.shiroumi.shiroplayer.room.entities.PlaylistMusicCrossRef

data class MusicInPlaylist(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistMusicCrossRef::class)
    )
    val musicList: List<Music>
)

