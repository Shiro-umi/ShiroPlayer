// IMusicService.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IMusicSercviceCommunication;
parcelable Music;


interface IMusicService {
    Music play(int index);
    Music playNext();
    Music getCurrentMusic();
    List<Music> getPlayList();
    Bitmap getMusicCover();
    void setCallback(IMusicSercviceCommunication callback);
}