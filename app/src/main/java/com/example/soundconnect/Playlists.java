package com.example.soundconnect;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class Playlists extends AppCompatActivity {
    DataBaseHelperPlaylists dbp;
    RecyclerView recyclerView;
    ArrayList<String> playlist_Name;
    PlaylistAdapter playlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MediaPlayerSingleton.getMediaPlayer() !=null){
            MediaPlayerSingleton.getMediaPlayer().release();
            MediaPlayerSingleton.setMediaPlayer(null);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlists);
        dbp = new DataBaseHelperPlaylists(this);
        recyclerView = findViewById(R.id.rvPlaylistList);
        playlist_Name=new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(this, playlist_Name,recyclerView);
        displayData();
        recyclerView.setAdapter(playlistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void gotoMenu(View v){
        Intent gotoMenu=new Intent(this, Menu.class);
        startActivity(gotoMenu);
    }

    public void gotoCreatePlaylist(View v){
        Intent gotoCreatePlaylist=new Intent(this, CreatePlaylist.class);
        startActivity(gotoCreatePlaylist);
    }

    public void gotoAllSongs(View v){
        Intent gotoAllSongs=new Intent(this, AllSongs.class);
        startActivity(gotoAllSongs);
    }
    public void deletePlaylist(View v) {

    }

    private void displayData() {
        Cursor cursor = dbp.getAllPlaylists();
        Log.d("cursor count",cursor.getCount()+"");
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No playlists yet", Toast.LENGTH_SHORT).show();
            return;
        }
        do {
            int columnIndex = cursor.getColumnIndex("PLAYLIST_NAME");
            if (columnIndex != -1) {
                Log.d("PLAYLIST_NAME", cursor.getString(columnIndex));
                playlist_Name.add(cursor.getString(columnIndex));
            }
        } while (cursor.moveToNext());
    }
}
