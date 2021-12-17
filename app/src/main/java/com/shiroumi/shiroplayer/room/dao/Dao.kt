package com.shiroumi.shiroplayer.room.dao

import androidx.room.*
import androidx.room.Dao
import com.shiroumi.shiroplayer.room.entities.Library
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import com.shiroumi.shiroplayer.room.entities.PlaylistMusicCrossRef
import com.shiroumi.shiroplayer.room.relation.MusicInPlayList

@Dao
abstract class Dao {
    @Query("select * from library")
    abstract fun selectLibraryPaths(): List<Library>

    @Insert(
        entity = Library::class,
        onConflict = OnConflictStrategy.IGNORE
    )
    abstract fun addLibraryPath(path: Library): Long

    @Query("select * from playlist")
    abstract fun selectPlaylists(): List<Playlist>

    @Transaction
    @Query("select * from playlist where name == :name")
    abstract fun selectPlaylist(name: String): MusicInPlayList

    @Query("select * from music")
    abstract fun selectDefaultPlaylist(): List<Music>

    @Insert(
        entity = Music::class,
        onConflict = OnConflictStrategy.IGNORE
    )
    abstract fun addMusic(musicList: List<Music>)
////
////    @Insert(entity = PlaylistMusicCrossRef::class, onConflict = IGNORE)
////    fun addMusicToPlaylist(crossRef: PlaylistMusicCrossRef)
//
//    @Insert(
//        entity = Music::class,
//        onConflict = OnConflictStrategy.REPLACE
//    )
//    fun addAllMusic(musicList: List<Music>)
//
//    @Query("select * from music")
//    fun getMusicList(): List<Music>
//
//    @Insert(entity = Playlist::class)
//    fun addPlaylist(playlist: Playlist): Long
//
//    @Query("select * from playlist")
//    fun getPlayLists(): List<Playlist>
////
////    @Transaction
////    @Query(
////        "select * from play_list where play_list.playlistId == :playListId"
////    )
////    fun getPlayListContent(playListId: Long): MusicInPlaylist
//
//
//    @Delete
//    fun deleteMusic(music: Music)
}