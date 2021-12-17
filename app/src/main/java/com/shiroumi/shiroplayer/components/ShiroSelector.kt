package com.shiroumi.shiroplayer.components


import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import com.shiroumi.shiroplayer.arch.application
import com.shiroumi.shiroplayer.composable.common.toFile
import com.shiroumi.shiroplayer.room.RoomManager
import com.shiroumi.shiroplayer.room.entities.Library
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.room.entities.Playlist
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.net.URI


class ShiroSelector {
    // DAO
    private val dao = RoomManager.db.dao()

//    /**
//     * 查询音乐库
//     */
//    fun selectLibraries(): List<Music> = TODO("查询Music表")

    /**
     * 查询所有playlist
     */
    fun selectPlaylists(): List<Playlist> = dao.selectPlaylists()

    /**
     * 查询特定playlist内容
     * @param name
     */
    fun selectPlaylist(name: String): List<Music> =
        if (name.isNotBlank() && name != "Default") {
            dao.selectPlaylist(name).musicList
        } else {
            dao.selectDefaultPlaylist()
        }

    /**
     * 更新库
     */
    fun updateLibrary(context: Context) {
        val resources = mutableListOf<Music>()
        selectLibraryPaths().forEach { path ->
            DocumentFile.fromTreeUri(context, Uri.parse(path))?.listFiles()?.forEach { file ->
                if (file.isFile && MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(file.type) == "mp3"
                ) {
                    val metaRetriever = MediaMetadataRetriever()
                    val tmpFile = file.uri.toFile(application)
                    metaRetriever.setDataSource(tmpFile.absolutePath)
                    tmpFile.convertToMusic(metaRetriever).apply {
                        uri = file.uri.toString()
                        resources.add(this)
                    }
                    metaRetriever.release()
                    tmpFile.delete()
                }
            }
        }
        dao.addMusic(resources)
    }

    /**
     * 添加库路径
     */
    fun addLibraryPath(path: String): Boolean = dao.addLibraryPath(Library(path = path)) > 0

    /**
     * 查询库路径列表
     */
    fun selectLibraryPaths(): List<String> = dao.selectLibraryPaths().map { it.path }
}