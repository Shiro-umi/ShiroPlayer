// IMusicServiceCommunication.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IBeans;

interface IMusicServiceCommunication {
    // music & player
    void onMusicPlaying(float process);
    void onSeekDone();
    void onMusicChanged(in MusicInfo musicInfo);

    // database callback
    void onMusicRefreshDone();
}