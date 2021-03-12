package com.shiroumi.shiroplayer

class Song {
    /**
     * 歌手
     */
    var singer: String = ""

    /**
     * 歌曲名
     */
    var song: String = ""

    /**
     * 专辑名
     */
    var album: String = ""

    /**
     * 专辑图片
     */
    var album_art: String = ""

    /**
     * 歌曲的地址
     */
    var path: String = ""

    /**
     * 歌曲长度
     */
    var duration = 0

    /**
     * 歌曲的大小
     */
    var size: Long = 0

    override fun toString(): String {
        return "$song+$album+$album_art+$path+$duration+$size"
    }
}