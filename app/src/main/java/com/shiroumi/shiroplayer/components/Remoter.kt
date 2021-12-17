package com.shiroumi.shiroplayer.components
//
//import android.content.ContentResolver
//import android.content.Context
//import android.graphics.Bitmap
//import android.media.MediaMetadataRetriever
//import android.net.Uri
//import android.util.Log
//import androidx.documentfile.provider.DocumentFile
//import com.shiroumi.shiroplayer.room.entities.Music
//import com.shiroumi.shiroplayer.MusicInfo
//import com.shiroumi.shiroplayer.room.entities.Playlist
//
//
//class Remoter(
//    private val context: Context,
//    contentResolver: ContentResolver
//) {
//    private val selector = MusicSelector(contentResolver)
//    var currentIndex = Int.MIN_VALUE
//    var playList: List<Music>? = null
//        private set
//
//    var currentMusic: Music? = null
//        get() = if (currentIndex == Int.MIN_VALUE) null else playList?.get(currentIndex)
//        private set
//
//    var currentMusicCover: Bitmap? = null
//        get() = currentMusic?.getBlurryCover(context)
//        private set
//
//    var currentMusicInfo: MusicInfo? = null
//        get() = MusicInfo(currentMusic, currentMusicCover, currentIndex)
//        private set
//
//    var currentPlaylist: Playlist? = null
//        private set
//
//    var playMode: PlayMode = PlayMode.NORMAL
//        set(value) {
//            field = value
//            when (value) {
//                PlayMode.NORMAL -> completeCallback = {
//                    ++currentIndex
//                    currentMusic?.play(context)
//                    changeMusicCallback?.invoke(
//                        MusicInfo(currentMusic, currentMusicCover, currentIndex)
//                    )
//                }
//                PlayMode.SINGLE -> {
//                }
//                PlayMode.RANDOM -> {
//                }
//            }
//        }
//
//    var onRefreshMusicDone: (() -> Unit)? = null
//    var onMusicDeleted: (() -> Unit)? = null
//
//    init {
//        playList = selector.getLocalMusicList { fromMediaStore ->
//            addAllMusic(fromMediaStore)
//        }
//    }
//
//    fun play(index: Int): MusicInfo? {
//        val playList = playList ?: return null
//        if (playList.isEmpty()) return null
//        var succeed = false
//        doWithNewIndexAfterStop(
//            when {
//                (index >= playList.size) -> 0
//                (index < 0) -> playList.size - 1
//                else -> index
//            }
//        ) {
//            currentMusic?.run {
//                succeed = play(context = context)
//            }
//        }
//        return when (succeed) {
//            true -> MusicInfo(currentMusic, currentMusicCover, currentIndex)
//            false -> null
//        }
//    }
//
//    fun playNext(): MusicInfo? = play(if (currentIndex == Int.MIN_VALUE) 0 else ++currentIndex)
//
//    fun playPrev(): MusicInfo? =
//        play(if (currentIndex != Int.MIN_VALUE) --currentIndex else currentIndex)
//
//    fun pause() = player.doPause()
//
//    fun resume() = player.doResume()
//
//    fun seekTo(target: Long) = player.doSeekTo(target.toInt())
//
//    fun stop() = player.doStop()
//
//    private fun doWithNewIndexAfterStop(newIndex: Int, block: () -> Unit) {
//        player.doStop()
//        currentIndex = newIndex
//        block.invoke()
//    }
//
//    fun refreshMusic() {
//        selector.refreshMusic {
//            playList = this
//            onRefreshMusicDone?.invoke()
//        }
//    }
//
//    fun refreshLocalMusic() {
//        playList = selector.getLocalMusicList()
//    }
//
//    fun deleteMusic(music: Music) {
//        selector.deleteMusic(music)
//        onMusicDeleted?.invoke()
//    }
//
//    fun updateMusicStore(uri: Uri) {
//        val files = DocumentFile.fromTreeUri(context, uri)?.listFiles()
//        files?.forEach {
//            Log.wtf("asdasdasd", it.name)
//        }
//    }
//}