package com.shiroumi.shiroplayer.components

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.annotation.StringDef
import com.shiroumi.shiroplayer.IMusicSercviceCommunication
import com.shiroumi.shiroplayer.Music

const val PLAY_MODE_NORMAL = "play_mode:normal"
const val PLAY_MODE_SINGLE = "play_mode:single"
const val PLAY_MODE_RANDOM = "play_mode:random"

@StringDef(
    PLAY_MODE_NORMAL,
    PLAY_MODE_SINGLE,
    PLAY_MODE_RANDOM
)
@Retention(AnnotationRetention.SOURCE)
annotation class PlayMode

class Remoter(
    private val context: Context,
    contentResolver: ContentResolver
) {
    private val selector = MusicSelector(contentResolver)
    private var currentIndex = -1
    private var realPlayList: List<PlayListNode?>? = null
    private var retriever: MediaMetadataRetriever? = null
    var playList: List<Music>? = null
        private set

    private val currentNode: PlayListNode?
        get() = realPlayList?.get(currentIndex)

    var currentMusic: Music? = null
        get() = currentNode?.music
        private set

    var currentMusicCover: Bitmap? = null
        get() {
            retriever?.release()
            return currentMusic?.getBlurryCover(context)
        }
        private set

    var callback: IMusicSercviceCommunication? = null

    init {
        selector.updatePlayList(PLAY_MODE_NORMAL)?.apply {
            realPlayList = first
            playList = second
            if (realPlayList?.size ?: 0 > 0) {
                currentIndex = 0
            }
        }
    }

    fun play(index: Int = -1) {
        val doPlay: () -> Unit = {
            currentNode?.processCallback = callback
            currentNode?.play(context)
        }
        if (index == -1) {
            doWithNewIndexAfterStop(0) { doPlay() }
            return
        }
        doWithNewIndexAfterStop(index) { doPlay() }
    }

    fun playNext() {
        play(++currentIndex)
    }

    fun playPrev() {
        play(--currentIndex)
    }

    private fun doWithNewIndexAfterStop(newIndex: Int, block: () -> Unit) {
        currentNode?.stop()
        currentIndex = newIndex
        block.invoke()
    }
}