package com.example.soundconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Pair extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pair);
    }

    public void gotoMenu(View v){
        Intent gotoMenu=new Intent(this, Menu.class);
        startActivity(gotoMenu);
    }

    public void gotoCreatePlaylist(View v){
        Intent gotoCreatePlaylist=new Intent(this, CreatePlaylist.class);
        startActivity(gotoCreatePlaylist);
    }
}