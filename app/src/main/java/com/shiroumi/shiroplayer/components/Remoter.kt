package com.shiroumi.shiroplayer.components

import android.content.ContentResolver
import android.content.Context
import com.shiroumi.shiroplayer.Music

class Remoter(
    private val context: Context,
    contentResolver: ContentResolver
) {
    private val selector = MusicSelector(contentResolver)
    private var currentNode: PlayListNode? = null

    init {
        currentNode = selector.updatePlayList()
    }

    fun play() {
        stopFirst { currentNode?.play(context) }
    }

    fun playNext() {
        stopFirst { currentNode = currentNode?.next()?.play(context) }
    }

    fun playPrev() {
        stopFirst { currentNode = currentNode?.prev()?.play(context) }
    }

    private fun stopFirst(block: () -> Unit) {
        currentNode?.stop()
        block.invoke()
    }

    fun current(): Music? {
        return currentNode?.getMusic()
    }
}