// IMusicServiceCommunication.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.room.entities.Music;

interface IShiroServiceCallback {
    // music & player
    void onMusicPlaying(float process);
    void onSeekDone();
    void onMusicChanged(in int index);

    // database callback
    void onLibraryUpdated();
    void onMusicRefreshDone();
    void onUriResourceUnavilable();
    void onMusicDeleted();
}