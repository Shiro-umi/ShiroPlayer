package com.shiroumi.shiroplayer.service
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.IBinder
//import com.shiroumi.shiroplayer.IMusicService
//import com.shiroumi.shiroplayer.IMusicServiceCommunication
//import com.shiroumi.shiroplayer.room.entities.Music
//import com.shiroumi.shiroplayer.MusicInfo
//import com.shiroumi.shiroplayer.arch.service.BaseService
//import com.shiroumi.shiroplayer.components.*
//import com.shiroumi.shiroplayer.room.RoomManager
//
//class MusicService : BaseService() {
//    lateinit var remoter: Remoter
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(newBase)
//        RoomManager.init(this)
//        remoter = Remoter(this, contentResolver)
//    }
//
//    override fun onBind(intent: Intent?): IBinder = token
//
//    override fun onUnbind(intent: Intent?): Boolean = super.onUnbind(intent)
//
//    private val token = object : IMusicService.Stub() {
//        override fun setPlayMode(playMode: PlayMode) {
//            remoter.playMode = playMode
//        }
//
//        override fun play(int: Int): MusicInfo? = remoter.play(int)
//
//        override fun playNext(): MusicInfo? = remoter.playNext()
//
//        override fun playPrev(): MusicInfo? = remoter.playPrev()
//
//        override fun getCurrentMusicInfo(): MusicInfo? = remoter.currentMusicInfo
//
//        override fun pause() = remoter.pause()
//
//        override fun resume() = remoter.resume()
//
//        override fun seekTo(target: Long) = remoter.seekTo(target)
//
//        override fun stop() = remoter.stop()
//
//        override fun getCurrentMusic(): Music? = remoter.currentMusic
//
//        override fun getCurrentIndex(): Int = remoter.currentIndex
//
//        override fun getPlayList(): List<Music>? = remoter.playList
//
//        override fun getMusicCover(): Bitmap? = remoter.currentMusicCover
//
//        override fun refreshMusic() = remoter.refreshMusic()
//
//        override fun refreshLocalMusic() = remoter.refreshLocalMusic()
//
//        override fun updateMusicStore(uri: String?) = remoter.updateMusicStore(Uri.parse(uri))
//
//        override fun deleteMusic(music: Music?) = music?.let { m ->
//            remoter.deleteMusic(m)
//        } ?: Unit
//
//        override fun setCallback(callback: IMusicServiceCommunication?): Unit = callback?.run {
////            processCallback = { process -> onMusicPlaying(process) }
////            seekCallback = this::onSeekDone
////            changeMusicCallback = { musicInfo -> onMusicChanged(musicInfo) }
////            remoter.onRefreshMusicDone = this::onMusicRefreshDone
////            onUriResourceUnavailable = this::onUriResourceUnavilable
////            remoter.onMusicDeleted = this::onMusicDeleted
//        } ?: Unit
//    }
//}