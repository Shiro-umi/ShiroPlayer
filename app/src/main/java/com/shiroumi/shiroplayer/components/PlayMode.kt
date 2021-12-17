package com.shiroumi.shiroplayer.components

import android.util.SparseArray
import com.shiroumi.shiroplayer.room.entities.Music

enum class PlayMode(
    val sort: List<Music>.() -> Pair<PlaylistNode?, SparseArray<PlaylistNode?>>?
) {
    NORMAL(sort = sortNormal),
    SINGLE(sort = sortSingle),
    RANDOM(sort = sortRandom)
}

val sortNormal: List<Music>.() -> Pair<PlaylistNode?, SparseArray<PlaylistNode?>>? =
    { buildPlayList(this) }

val sortSingle: List<Music>.() -> Pair<PlaylistNode?, SparseArray<PlaylistNode?>>? =
    { buildPlayList(this) }

val sortRandom: List<Music>.() -> Pair<PlaylistNode?, SparseArray<PlaylistNode?>>? =
    { buildPlayList(shuffled()) }

/**
 * 将列表转换为链表
 * @param playlist 播放列表
 */
fun buildPlayList(playlist: List<Music>): Pair<PlaylistNode?, SparseArray<PlaylistNode?>>? {
    if (playlist.isEmpty()) return null

    val playlistIndexed = SparseArray<PlaylistNode?>()

    val hair = PlaylistNode(Music())
    var head: PlaylistNode?
    var prev: PlaylistNode? = null
    hair.next = prev

    // 初始化head
    head = PlaylistNode(playlist[0])
    playlistIndexed.put(0, head)
    hair.next = head

    for (i in 1 until playlist.size) {
        prev = head
        head = PlaylistNode(playlist[i])
        playlistIndexed.put(i, head)
        head.prev = prev
        prev?.next = head
    }
    head?.next = hair.next
    hair.next?.prev = head

    return hair.next to playlistIndexed
}