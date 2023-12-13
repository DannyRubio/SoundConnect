package com.example.soundconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }
    public void gotoPairDevices(View v){
        Intent gotoPairDevices=new Intent(this, Pair.class);
        startActivity(gotoPairDevices);
    }

    public void gotoPlaylists(View v){
        Intent gotoPlaylists=new Intent(this, Playlists.class);
        startActivity(gotoPlaylists);
    }
}
