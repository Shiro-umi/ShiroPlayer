// IMusicServiceCommunication.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.room.entities.Music;

interface IMusicServiceCommunication {
    // music & player
    void onMusicPlaying(float process);
    void onSeekDone();
    void onMusicChanged(in Music musicInfo);

    // database callback
    void onMusicRefreshDone();
    void onUriResourceUnavilable();
    void onMusicDeleted();
}