package com.example.soundconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreatePlaylist extends AppCompatActivity {
    DataBaseHelperSongs dbs;
    DataBaseHelperPlaylists dbp;
    RecyclerView recyclerView;
    ArrayList<String> song_Title, group_Name, file_Path;
    ArrayList<Integer> audio_resource_id;
    AllSongsAdapter allSongsAdapter;

    String songList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_playlists);
        dbs = new DataBaseHelperSongs(this);
        dbp = new DataBaseHelperPlaylists(this);
        song_Title = new ArrayList<>();
        group_Name = new ArrayList<>();
        file_Path = new ArrayList<>();
        audio_resource_id = new ArrayList<>();
        recyclerView = findViewById(R.id.rvSongsList);
        allSongsAdapter = new AllSongsAdapter(this, song_Title, group_Name, file_Path);
        recyclerView.setAdapter(allSongsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();
    }

    private void displayData() {
        Cursor cursor = dbs.getAllSongs();
        if (cursor.getCount() == 0) {
            findViewById(R.id.btnCreatePlaylist).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtCreatePlaylistBtn).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnGoToAllSongs).setVisibility(View.VISIBLE);
            findViewById(R.id.txtNoSongs).setVisibility(View.VISIBLE);
            findViewById(R.id.txtGoToPlaylistBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.btnGoToAllSongs).setVisibility(View.VISIBLE);
            findViewById(R.id.rvSongsList).setVisibility(View.INVISIBLE);
            return;
        }
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("SONG_TITLE");
            if (columnIndex != -1) {
                song_Title.add(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex("GROUP_NAME");
            if (columnIndex != -1) {
                group_Name.add(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex("FILE_PATH");
            Log.d("FilePath", cursor.getString(columnIndex));
            Log.d("FileExists", new File(cursor.getString(columnIndex)).exists() + "");
            if (columnIndex != -1) {
                file_Path.add(cursor.getString(columnIndex));
            }
        }
    }


    public void gotoMenu(View v) {
        Intent gotoMenu = new Intent(this, Menu.class);
        startActivity(gotoMenu);
    }

    public void gotoPlaylists(View v) {
        Intent gotoPlaylists = new Intent(this, Playlists.class);
        startActivity(gotoPlaylists);
    }

    public void gotoAllSongs(View v) {
        Intent gotoAllSongs = new Intent(this, AllSongs.class);
        startActivity(gotoAllSongs);
    }

    int requestCodeModifyPlaylistName = 1;
    PlaylistModel pm = new PlaylistModel();

    public void createPlaylist(View view) {
        Log.d("createPlaylist", "createPlaylist");
        Intent gotoNamePlaylist = new Intent(CreatePlaylist.this, NamePlaylist.class);
        startActivityForResult(gotoNamePlaylist, requestCodeModifyPlaylistName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DataBaseHelperPlaylists dbhP = new DataBaseHelperPlaylists(this);
        Context context = getApplicationContext();
        if (requestCode == this.requestCodeModifyPlaylistName && resultCode == Activity.RESULT_OK) {

            if (data != null) {

                pm.setPlaylistName(data.getStringExtra("playlistName"));

                ArrayList<Integer> songNums = allSongsAdapter.getSelectedSongs();

                for (int songNum : songNums) {
                    songList += song_Title.get(songNum) + " - " + group_Name.get(songNum) + ";";
                }

                pm.setSongList(songList);
                pm.setDateCreated(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").format(new Date()));
                String response = dbhP.insertPlaylist(pm);
                Log.d("testfr1", "1  " + response);
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(context, "Song info empty, try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Error adding song resultCode:" + resultCode + "\n", Toast.LENGTH_SHORT).show();
            setPMtoNull(pm);
        }
    }

    private void setPMtoNull(PlaylistModel pm) {
        pm.setId(-1);
        pm.setPlaylistName(null);
        pm.setSongList(null);
        pm.setDateCreated(null);
    }
}
