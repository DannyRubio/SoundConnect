package com.example.soundconnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class DataBaseHelperMASTER extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "soundConnect.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;


    public DataBaseHelperMASTER(@Nullable Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("onCreate","DataBaseHelperMASTER ");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate","onCreate MASTER");
        createSongsTable(db);
        createPlaylistsTable(db);
    }

    private void createSongsTable(SQLiteDatabase db) {
        String TABLE_NAME = "SONGS";
        String COLUMN_ID = "ID";
        String COLUMN_SONG_TITLE = "SONG_TITLE";
        String COLUMN_GROUP_NAME = "GROUP_NAME";
        String COLUMN_SONG_NAME = "SONG_NAME";
        String COLUMN_FILE_PATH = "FILE_PATH";
        String COLUMN_DATE_UPLOADED = "DATE_UPLOADED";

        String createTableStatement = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SONG_TITLE + " TEXT, "
                + COLUMN_GROUP_NAME + " TEXT, "
                + COLUMN_SONG_NAME + " TEXT, "
                + COLUMN_FILE_PATH + " TEXT , "
                + COLUMN_DATE_UPLOADED + " TEXT);";

        db.execSQL(createTableStatement);
    }

    private void createPlaylistsTable(SQLiteDatabase db) {
        String TABLE_NAME = "PLAYLISTS";
        String COLUMN_ID = "ID";
        String COLUMN_PLAYLIST_NAME= "PLAYLIST_NAME";
        String COLUMN_DATE_CREATED = "DATE_CREATED";
        String COLUMN_SONG_LIST = "SONG_LIST";

        String createTableStatement = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLAYLIST_NAME + " TEXT, "
                + COLUMN_SONG_LIST + " TEXT, "
                + COLUMN_DATE_CREATED + " TEXT);";
        db.execSQL(createTableStatement);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
