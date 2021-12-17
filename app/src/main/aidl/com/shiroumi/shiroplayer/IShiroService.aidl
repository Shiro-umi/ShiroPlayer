// IMusicService.aidl
package com.shiroumi.shiroplayer;
import com.shiroumi.shiroplayer.IShiroServiceCallback;
import com.shiroumi.shiroplayer.room.entities.Music;
import com.shiroumi.shiroplayer.room.entities.Playlist;

interface IShiroService {
    // Library
    boolean addLibraryPath(String path);
    void updateLibrary();
    List<String> getLibraryPaths();

    // playlist
    List<Playlist> getPlaylists();
    List<Music> getPlaylist(String playlistName);

    // play mode
    void setPlayMode(int playModeIndex);

    // play control
    Music play(int index);
    void pause();
    void resume();
    void seekTo(int position);
    void stop();

    Bitmap getCover();
    Bitmap getBlurryCover();


//    Music getCurrentMusic();
//    int getCurrentIndex();
//    Bitmap getMusicCover();

//    // database
//    void refreshMusic();
//    void refreshLocalMusic();
//    void updateMusicStore(String uri);
//    void deleteMusic(out Music music);

    // callback
    void setCallback(in IShiroServiceCallback callback);
}