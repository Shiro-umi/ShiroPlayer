// IMusicService.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IMusicServiceCommunication;
parcelable Music;

interface IMusicService {
    Music play(int index);
    Music playNext();
    void pause();
    void resume();
    void seekTo(long position);
    Music getCurrentMusic();
    int getCurrentIndex();
    List<Music> getPlayList();
    Bitmap getMusicCover();
    void setCallback(IMusicServiceCommunication callback);
    void setPlayMode(int playMode);
}