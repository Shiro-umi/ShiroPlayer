package com.shiroumi.shiroplayer.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import com.shiroumi.shiroplayer.room.entities.Music
import android.renderscript.ScriptIntrinsicBlur


fun Music.getCover(
    context: Context,
    retriever: MediaMetadataRetriever? = MediaMetadataRetriever()
): Bitmap? {
    if (uri.isBlank()) return null
    retriever?.setDataSource(context, Uri.parse(uri))
    val bitmapData = retriever?.embeddedPicture ?: return null
    val cover = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
    retriever.release()
    return cover
}

fun Music.getBlurryCover(
    context: Context
): Bitmap? {
    val cover = getCover(context)
    cover ?: return null
    val renderScript = RenderScript.create(context)
    val allocationInput = Allocation.createFromBitmap(renderScript, cover)
    val allocationOutput = Allocation.createTyped(renderScript, allocationInput.type)
    val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    scriptIntrinsicBlur.apply {
        setInput(allocationInput)
        setRadius(20f)
        forEach(allocationOutput)
    }
    allocationOutput.copyTo(cover)
    renderScript.destroy()
    return cover
}