package com.example.soundconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NamePlaylist extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","onCreate");
        setContentView(R.layout.name_playlist);
        EditText pn = (EditText) findViewById(R.id.editTextPlaylistName);
        pn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    findViewById(R.id.lblErrorPlaylistName).setVisibility(View.INVISIBLE);
                }
            }
        });


        Button btnFinish = (Button) findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = true;
                String playlistName = ((EditText) (findViewById(R.id.editTextPlaylistName))).getText().toString().trim();

                if (playlistName.equals("")) {
                    TextView lblErrorGroup = findViewById(R.id.lblErrorPlaylistName);
                    lblErrorGroup.setText("Playlist name not specified");
                    findViewById(R.id.lblErrorPlaylistName).setVisibility(View.VISIBLE);
                    valid = false;
                }else if(playlistName.contains(".") || playlistName.contains("-")|| playlistName.contains(";")){
                    TextView lblErrorGroup = findViewById(R.id.lblErrorPlaylistName);
                    lblErrorGroup.setText("Can't contain - ,. or ;");
                    findViewById(R.id.lblErrorPlaylistName).setVisibility(View.VISIBLE);
                    valid = false;
                }

                if (valid) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("playlistName", playlistName);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}



