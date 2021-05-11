// IMusicService.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IMusicServiceCommunication;
import com.shiroumi.shiroplayer.IBeans;

interface IMusicService {
    MusicInfo play(int index);
    MusicInfo playNext();
    MusicInfo playPrev();
    MusicInfo getCurrentMusicInfo();
    void pause();
    void resume();
    void seekTo(long position);
    void stop();
    Music getCurrentMusic();
    int getCurrentIndex();
    List<Music> getPlayList();
    Bitmap getMusicCover();
    void setCallback(in IMusicServiceCommunication callback);
    void setPlayMode(int playMode);
}