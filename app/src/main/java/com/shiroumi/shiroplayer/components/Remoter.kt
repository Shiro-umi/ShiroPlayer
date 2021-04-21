package com.shiroumi.shiroplayer.components

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.shiroumi.shiroplayer.Music


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
    var currentIndex = -1
    private var retriever: MediaMetadataRetriever? = null
    var playList: List<Music>? = null
        private set

    var currentMusic: Music? = null
        get() = if (currentIndex == -1) null else playList?.get(currentIndex)
        private set

    var currentMusicCover: Bitmap? = null
        get() {
            retriever?.release()
            return currentMusic?.getBlurryCover(context)
        }
        private set

    var playMode: Int = PlayMode.NORMAL.value
        set(value) {
            field = value
            when (value) {
                PlayMode.NORMAL.value -> completeCallback = {
                    ++currentIndex
                    currentMusic?.play(context)
                    changeMusicCallback?.invoke()
                }
                PlayMode.SINGLE.value -> {
                }
                PlayMode.RANDOM.value -> {
                }
            }
        }

    init {
        playList = selector.updatePlayList(PlayMode.NORMAL)
    }

    fun play(index: Int = -1) {
        val doPlay: () -> Unit = {
            currentMusic?.play(context = context)
        }
        doWithNewIndexAfterStop(if (index == -1) 0 else index) { doPlay() }
    }

    fun playNext() {
        play(++currentIndex)
    }

    fun playPrev() {
        play(--currentIndex)
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

    private fun doWithNewIndexAfterStop(newIndex: Int, block: () -> Unit) {
        player.doStop()
        currentIndex = newIndex
        block.invoke()
    }
}