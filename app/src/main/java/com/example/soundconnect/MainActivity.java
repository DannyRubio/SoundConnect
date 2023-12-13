package com.example.soundconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    DataBaseHelperMASTER dbhm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","onCreate MAIN");
        dbhm=new DataBaseHelperMASTER(MainActivity.this);
        dbhm.getWritableDatabase();
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMenuActivity();
                finish();
            }
        }, 200);
    }

    private void startMenuActivity() {
        Intent gotoMenu = new Intent(this, Menu.class);
        startActivity(gotoMenu);
        finish();
    }
}