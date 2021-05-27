// IMusicService.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IMusicServiceCommunication;
import com.shiroumi.shiroplayer.IBeans;
import com.shiroumi.shiroplayer.room.entities.RoomEntities;

interface IMusicService {
    // play mode
    void setPlayMode(int playMode);

    // play control
    MusicInfo play(int index);
    MusicInfo playNext();
    MusicInfo playPrev();
    MusicInfo getCurrentMusicInfo();
    void pause();
    void resume();
    void seekTo(long position);
    void stop();

    // music & player info
    Music getCurrentMusic();
    int getCurrentIndex();
    List<Music> getPlayList();
    Bitmap getMusicCover();

    // database
    void refreshMusic();
    void updateMusicStore(String uri);

    // callback
    void setCallback(in IMusicServiceCommunication callback);
}