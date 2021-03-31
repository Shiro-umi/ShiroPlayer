package com.shiroumi.shiroplayer.components

import android.content.ContentResolver
import android.content.Context
import androidx.annotation.StringDef
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
    private var currentNode: PlayListNode? = null
    var currentMusic: Music? = null
        get() = currentNode?.music
        private set
    var indexContent = arrayListOf<Music>()
        private set

    init {
        currentNode = selector.updatePlayList(PLAY_MODE_NORMAL)
        buildIndexPlayList()
    }

    fun play() {
        stopFirst { currentNode?.play(context) }
    }

    fun playNext() {
        stopFirst { currentNode = currentNode?.next?.play(context) }
    }

    fun playPrev() {
        stopFirst { currentNode = currentNode?.prev?.play(context) }
    }

    private fun stopFirst(block: () -> Unit) {
        currentNode?.stop()
        block.invoke()
    }

    private fun buildIndexPlayList() {
        indexContent.clear()
        val head = currentNode
        var pointer = head
        while (pointer?.next != head) {
            pointer?.music?.let {
                indexContent.add(it)
            }
            pointer = pointer?.next
        }
    }
}