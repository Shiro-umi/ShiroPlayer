package com.shiroumi.shiroplayer.components

import android.content.Context
import android.util.Log
import android.util.SparseArray
import com.shiroumi.shiroplayer.arch.application
import com.shiroumi.shiroplayer.room.entities.Music

class ShiroRemoter(val context: Context = application) {

    // 播放列表
    private var playlistForeground = listOf<Music>()
    private var playlistBackground = SparseArray<PlaylistNode?>()

    // 数据库操作工具
    private var selector = ShiroSelector()

    // 当前Music
    private var currentMusic: Music? = null
    private var currentMusicBackground: PlaylistNode? = null

    // 播放模式
    var playMode: PlayMode = PlayMode.NORMAL

    /**
     * 更新音乐库
     */
    fun updateLibrary() = selector.updateLibrary(context)

    /**
     * 添加新的Library路径
     * @param path Library路径
     */
    fun addLibraryPath(path: String) = selector.addLibraryPath(path).also {
        updateLibrary()
    }

    /**
     * 获取Library路径
     */

    fun getLibraryPath() = selector.selectLibraryPaths()

    /**
     * 查询所有播放列表
     */
    fun getPlaylists() = selector.selectPlaylists()

    /**
     * 查询特定playlist内容，同时根据playMode初始化播放列表
     * @param name
     */
    fun getPlaylist(name: String): List<Music> = with(playMode) {
        playlistForeground = emptyList()
        playlistBackground.clear()
        playlistForeground = selector.selectPlaylist(name)

        sort(playlistForeground)?.run {
            currentMusicBackground = first
            playlistBackground = second
            currentMusic = currentMusicBackground?.music
        }
        return@with playlistForeground
    }

    /**
     * 播放
     * @param index playlist中的索引
     */
    fun play(index: Int): Music? {
        val nextNode = playlistBackground[index]
        currentMusicBackground = nextNode
        currentMusic = nextNode?.play()
        return currentMusic
    }

    /**
     * 播放下一首
     */
    fun playNext(): Int {
        val nextNode = currentMusicBackground?.next
        currentMusicBackground = nextNode
        currentMusic = nextNode?.play()
        return playlistForeground.indexOf(currentMusic)
    }

    /**
     * 播放上一首
     */
    private fun playPrev() = currentMusicBackground?.prev?.play()

//    /**
//     * 获取全部音乐列表
//     */
//    private fun getMusicLibraryList() = selector.selectLibraries()
}
