package com.shiroumi.shiroplayer.composable.common

import android.content.Context
import android.net.Uri
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import kotlin.contracts.contract

val Int.f
    get() = toFloat()

val Float.i
    get() = toInt()

fun Uri.toFile(context: Context): File = File.createTempFile("shiro", ".tmp").apply {
    val inputStream = context.contentResolver.openInputStream(this@toFile)
    val outputStream = FileOutputStream(this)
    IOUtils.copy(inputStream, outputStream)
    inputStream?.close()
    outputStream.close()
}

fun <T> List<T>.toCircleLinkedList(): ListNode<T>? {
    if (isEmpty()) return null
    val start = ListNode(first())
    var head = start
    var prev = start
    for (i in 1 until size) {
        head = ListNode(this[i])
        head.prev = prev
        prev.next = head
        prev = prev.next!!
    }
    head.next = start
    start.prev = head
    return start
}

data class ListNode<T>(
    var data: T,
    var next: ListNode<T>? = null,
    var prev: ListNode<T>? = null
)