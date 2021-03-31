// IMusicService.aidl
package com.shiroumi.shiroplayer;

parcelable Music;

// Declare any non-default types here with import statements

interface IMusicService {
    Music play();
    Music playNext();
    Music getCurrentMusic();
    List<Music> getIndexContent();
}