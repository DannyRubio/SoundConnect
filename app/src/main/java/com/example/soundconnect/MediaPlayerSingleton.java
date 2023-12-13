package com.example.soundconnect;

import android.media.MediaPlayer;
import android.util.Log;
import java.io.IOException;

public class MediaPlayerSingleton {

//    private static MediaPlayer mediaPlayer;
    private static MediaPlayer mediaPlayer;
    private static String dataSource;

    private MediaPlayerSingleton() {

    }

    public static MediaPlayer getMediaPlayer() {
        return MediaPlayerSingleton.mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        MediaPlayerSingleton.mediaPlayer = mediaPlayer;
    }

    public static void setDataSource(String filePath){
        Log.d("MediaPlayer", "Source " + filePath);
        try {
            dataSource = filePath;
            if (MediaPlayerSingleton.mediaPlayer == null) {
                MediaPlayerSingleton.mediaPlayer = getInstance();
            }

            MediaPlayerSingleton.mediaPlayer.setDataSource(dataSource);
            MediaPlayerSingleton.mediaPlayer.prepare();
        } catch (IOException e) {
            Log.w("Error",e.getMessage());
        }
    }
    public static synchronized MediaPlayer getInstance() {
        if (MediaPlayerSingleton.mediaPlayer == null) {
            MediaPlayerSingleton.mediaPlayer = new MediaPlayer();
        }
        return MediaPlayerSingleton.mediaPlayer;
    }

    public static synchronized boolean isPlaying() {
        return MediaPlayerSingleton.mediaPlayer.isPlaying();
    }

    public static synchronized boolean isInitialized() {
        return (MediaPlayerSingleton.mediaPlayer != null);
    }

    public static synchronized void start() {
        Log.d("MediaPlayerSingleton","start");
        MediaPlayerSingleton.mediaPlayer.start();
    }

    public static synchronized void pauseAudio() {
        if (MediaPlayerSingleton.mediaPlayer != null && MediaPlayerSingleton.mediaPlayer.isPlaying()) {
            MediaPlayerSingleton.mediaPlayer.pause();
        }
    }

    public static synchronized void release() {
        MediaPlayerSingleton.mediaPlayer.release();
    }

    public static synchronized String[] getTotalSongTime() {
        Log.d("MediaPlayerSingleton.mediaPlayer.getDuration()",mediaPlayer.getDuration()+"");
        Log.d("MediaPlayerSingleton.mediaPlayer.getDuration() null?",(MediaPlayerSingleton.getMediaPlayer()==null)+"");
        String mins = String.valueOf((MediaPlayerSingleton.mediaPlayer.getDuration() / 1000) / 60);
        String secs = String.valueOf((MediaPlayerSingleton.mediaPlayer.getDuration() / 1000) % 60);
        if (Integer.parseInt(secs) < 10) {
            secs = "0" + secs;
        }
        String[] result = {mins, secs};
        return result;
    }
    public static synchronized void logHashCode(){
        Log.d("hashcode SINGLETON",MediaPlayerSingleton.mediaPlayer+"");

    }
}
