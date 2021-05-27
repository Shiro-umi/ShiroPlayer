package com.shiroumi.shiroplayer.room.dao

import androidx.room.*
import androidx.room.Dao
import androidx.room.OnConflictStrategy.IGNORE
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import com.shiroumi.shiroplayer.room.entities.PlaylistMusicCrossRef
import com.shiroumi.shiroplayer.room.relation.MusicInPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.selects.select

@Dao
interface Dao {
//
//    @Insert(entity = PlaylistMusicCrossRef::class, onConflict = IGNORE)
//    fun addMusicToPlaylist(crossRef: PlaylistMusicCrossRef)

    @Insert(
        entity = Music::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    fun addAllMusic(musicList: List<Music>)

    @Query("select * from music")
    fun getMusicList(): List<Music>

    @Insert(entity = Playlist::class)
    fun addPlaylist(playlist: Playlist): Long

    @Query("select * from playlist")
    fun getPlayLists(): List<Playlist>
//
//    @Transaction
//    @Query(
//        "select * from play_list where play_list.playlistId == :playListId"
//    )
//    fun getPlayListContent(playListId: Long): MusicInPlaylist


}