// IMusicService.aidl
package com.shiroumi.shiroplayer;

parcelable Music;

// Declare any non-default types here with import statements

interface IMusicService {
    Music play(int index);
    Music playNext();
    Music getCurrentMusic();
    List<Music> getPlayList();
    Bitmap getMusicCover();
}