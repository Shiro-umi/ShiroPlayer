package com.shiroumi.shiroplayer.components

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.shiroumi.shiroplayer.IMusicSercviceCommunication
import com.shiroumi.shiroplayer.Music


enum class PlayMode {
    NORMAL,
    SINGLE,
    RANDOM
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
        get() = playList?.get(currentIndex)
        private set

    var currentMusicCover: Bitmap? = null
        get() {
            retriever?.release()
            return currentMusic?.getBlurryCover(context)
        }
        private set

    var callback: IMusicSercviceCommunication? = null

    init {
        playList = selector.updatePlayList(PlayMode.NORMAL)
        if (!playList.isNullOrEmpty()) {
            currentIndex = 0
        }
    }

    fun play(index: Int = -1) {
        val doPlay: () -> Unit = {
            currentMusic?.play(
                context = context,
                processCallback = callback
            )
        }
        doWithNewIndexAfterStop(if(index == -1) 0 else index) { doPlay() }
    }

    fun playNext() {
        play(++currentIndex)
    }

    fun playPrev() {
        play(--currentIndex)
    }

    private fun doWithNewIndexAfterStop(newIndex: Int, block: () -> Unit) {
        currentMusic?.stop()
        currentIndex = newIndex
        block.invoke()
    }
}