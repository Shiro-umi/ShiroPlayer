package com.shiroumi.shiroplayer.components

import android.net.Uri
import com.shiroumi.shiroplayer.room.entities.Music

class PlaylistNode(
    val music: Music
) {
    var next: PlaylistNode? = null
    var prev: PlaylistNode? = null

    /**
     * 播放当前Node对应的Music
     */
    fun play(): Music {
        Uri.parse(music.uri).play()
        return music
    }
}