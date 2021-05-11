package com.shiroumi.shiroplayer.components

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.MusicInfo


enum class PlayMode(val value: Int) {
    NORMAL(0),
    SINGLE(1),
    RANDOM(2)
}

class Remoter(
    private val context: Context,
    contentResolver: ContentResolver
) {
    private val selector = MusicSelector(contentResolver)
    var currentIndex = Int.MIN_VALUE
    private var retriever: MediaMetadataRetriever? = null
    var playList: List<Music>? = null
        private set

    var currentMusic: Music? = null
        get() = if (currentIndex == Int.MIN_VALUE) null else playList?.get(currentIndex)
        private set

    var currentMusicCover: Bitmap? = null
        get() {
            retriever?.release()
            return currentMusic?.getBlurryCover(context)
        }
        private set

    var currentMusicInfo: MusicInfo? = null
        get() = MusicInfo(currentMusic, currentMusicCover, currentIndex)
        private set

    var playMode: Int = PlayMode.NORMAL.value
        set(value) {
            field = value
            when (value) {
                PlayMode.NORMAL.value -> completeCallback = {
                    ++currentIndex
                    currentMusic?.play(context)
                    changeMusicCallback?.invoke(
                        MusicInfo(
                            currentMusic,
                            currentMusicCover,
                            currentIndex
                        )
                    )
                }
                PlayMode.SINGLE.value -> {
                }
                PlayMode.RANDOM.value -> {
                }
            }
        }

    init {
        playList = selector.updatePlayList()
    }

    fun play(index: Int): MusicInfo? {
        val playList = playList ?: return null
        if (playList.isEmpty()) return null
        doWithNewIndexAfterStop(
            when {
                (index >= playList.size) -> 0
                (index < 0) -> playList.size - 1
                else -> index
            }
        ) {
            currentMusic?.play(context = context)
        }
        return MusicInfo(
            currentMusic,
            currentMusicCover,
            currentIndex
        )
    }

    fun playNext(): MusicInfo? {
        return play(if (currentIndex == Int.MIN_VALUE) 0 else ++currentIndex)
    }

    fun playPrev(): MusicInfo? {
        return play(if (currentIndex != Int.MIN_VALUE) --currentIndex else currentIndex)
    }

    fun pause() {
        player.doPause()
    }

    fun resume() {
        player.doResume()
    }

    fun seekTo(target: Long) {
        player.doSeekTo(target.toInt())
    }

    fun stop() {
        player.doStop()
    }

    private fun doWithNewIndexAfterStop(newIndex: Int, block: () -> Unit) {
        player.doStop()
        currentIndex = newIndex
        block.invoke()
    }
}