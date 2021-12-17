package com.shiroumi.shiroplayer.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.mpatric.mp3agic.Mp3File
import com.shiroumi.shiroplayer.arch.application
import com.shiroumi.shiroplayer.room.entities.Music
import java.io.File


val currentCover: Bitmap?
    get() = with(MediaMetadataRetriever()) {
        val file = mediaFile
        file ?: return null
        setDataSource(file.absolutePath)
        val bitmapData = embeddedPicture ?: return null
        val cover = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
        release()
        return cover
    }

val currentBlurryCover: Bitmap?
    get() {
        val cover = currentCover ?: return null
        val renderScript = RenderScript.create(application)
        val allocationInput = Allocation.createFromBitmap(renderScript, cover)
        val allocationOutput = Allocation.createTyped(renderScript, allocationInput.type)
        val scriptIntrinsicBlur =
            ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        scriptIntrinsicBlur.apply {
            setInput(allocationInput)
            setRadius(20f)
            forEach(allocationOutput)
        }
        allocationOutput.copyTo(cover)
        renderScript.destroy()
        return cover
    }

fun File.convertToMusic(retriever: MediaMetadataRetriever): Music = with(retriever) {
    setDataSource(absolutePath)
    return Music(
        musicTitle = extractTitle() ?: "Unknown",
        artist = extractArtist() ?: "Unknown",
        album = extractAlbum() ?: "Unknown",
        duration = extractDuration()?.toFloat() ?: 0f,
    )
}

private fun MediaMetadataRetriever.extractTitle() =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

private fun MediaMetadataRetriever.extractArtist() =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

private fun MediaMetadataRetriever.extractAlbum() =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)

private fun MediaMetadataRetriever.extractDuration() =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)