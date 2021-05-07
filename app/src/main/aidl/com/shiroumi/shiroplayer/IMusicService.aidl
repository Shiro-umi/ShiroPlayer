// IMusicService.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IMusicServiceCommunication;
parcelable Music;

interface IMusicService {
    Music play(int index);
    Music playNext();
    Music playPrev();
    void pause();
    void resume();
    void seekTo(long position);
    void stop();
    Music getCurrentMusic();
    int getCurrentIndex();
    List<Music> getPlayList();
    Bitmap getMusicCover();
    void setCallback(IMusicServiceCommunication callback);
    void setPlayMode(int playMode);
}