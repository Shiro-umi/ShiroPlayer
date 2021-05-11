// IMusicServiceCommunication.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IBeans;

interface IMusicServiceCommunication {
    void onMusicPlaying(float process);
    void onSeekDone();
    void onMusicChanged(in MusicInfo musicInfo);
}