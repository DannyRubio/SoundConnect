package com.example.soundconnect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddSongTitleGroupName extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_name_group);

        EditText st = (EditText) findViewById(R.id.editTextSongTitle);

        st.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    findViewById(R.id.lblErrorSong).setVisibility(View.INVISIBLE);
                }
            }
        });

        EditText gn = (EditText) findViewById(R.id.editTextGroupName);

        gn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    findViewById(R.id.lblErrorGroup).setVisibility(View.INVISIBLE);
                }
            }
        });

        Button btnFinish = (Button) findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = true;
                String groupName = (String) ((EditText) (findViewById(R.id.editTextGroupName))).getText().toString().trim();
                String songTitle = (String) ((EditText) (findViewById(R.id.editTextSongTitle))).getText().toString().trim();

                if (groupName.equals("")) {
                    TextView lblErrorGroup = findViewById(R.id.lblErrorGroup);
                    lblErrorGroup.setText("Group name not specified");
                    findViewById(R.id.lblErrorGroup).setVisibility(View.VISIBLE);
                    valid = false;
                }else if(groupName.contains(".") || groupName.contains("-")|| groupName.contains(";")){
                    TextView lblErrorGroup = findViewById(R.id.lblErrorGroup);
                    lblErrorGroup.setText("Can't contain -, . or ;");
                    findViewById(R.id.lblErrorGroup).setVisibility(View.VISIBLE);
                    valid = false;
                }
                if (songTitle.equals("")) {
                    TextView lblErrorSong = findViewById(R.id.lblErrorSong);
                    lblErrorSong.setText("Song title not specified");
                    findViewById(R.id.lblErrorSong).setVisibility(View.VISIBLE);
                    valid = false;
                }else if(songTitle.contains(".") || songTitle.contains("-")){
                    TextView lblErrorSong = findViewById(R.id.lblErrorSong);
                    lblErrorSong.setText("Can't contain - or .");
                    findViewById(R.id.lblErrorSong).setVisibility(View.VISIBLE);
                    valid = false;
                }

                if (valid) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("groupName", groupName);
                    returnIntent.putExtra("songTitle", songTitle);

                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}



