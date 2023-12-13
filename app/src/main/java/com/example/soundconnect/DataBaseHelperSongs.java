package com.example.soundconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelperSongs extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "soundConnect.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "SONGS";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_SONG_TITLE = "SONG_TITLE";
    private static final String COLUMN_GROUP_NAME = "GROUP_NAME";
    private static final String COLUMN_SONG_NAME = "SONG_NAME";
    private static final String COLUMN_FILE_PATH = "FILE_PATH";
    private static final String COLUMN_DATE_UPLOADED = "DATE_UPLOADED";


    public DataBaseHelperSongs(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SONG_TITLE + " TEXT, "
                + COLUMN_GROUP_NAME + " TEXT, "
                + COLUMN_SONG_NAME + " TEXT, "
                + COLUMN_FILE_PATH + " TEXT , "
                + COLUMN_DATE_UPLOADED + " TEXT);";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String insertSong(SongsModel sm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SONG_TITLE, sm.getSongTitle());
        cv.put(COLUMN_GROUP_NAME, sm.getGroupName());
        cv.put(COLUMN_SONG_NAME, sm.getSongTitle()+" - "+sm.getGroupName());
        cv.put(COLUMN_FILE_PATH, sm.getFilePath());
        cv.put(COLUMN_DATE_UPLOADED, sm.getDateUploaded());

        String query = "Select " + COLUMN_SONG_TITLE + "," + COLUMN_GROUP_NAME + " from " + TABLE_NAME + " where " + COLUMN_SONG_TITLE + " = '" + sm.getSongTitle() + "' AND " + COLUMN_GROUP_NAME + " = '" + sm.getGroupName() + "'";

        Cursor cursor = db.rawQuery(query, null);
        String firstRowData = "empty";


        if (!cursor.moveToFirst()) {
            Log.w("cv", cv.toString());
            try {
                //  db.execSQL(query, values);
                Long insert = db.insert(TABLE_NAME, null, cv);
            } catch (android.database.SQLException e) {
                return "Error adding song " + e.getMessage();
            }
            return "Song added";
        }
        return "The song title and group name already exist";
    }

    public Cursor getAllSongs() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAllPlaylistSongs(String list) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] songs=list.split(";");
        StringBuilder query = new StringBuilder("SELECT * from " + TABLE_NAME +" WHERE "+COLUMN_SONG_NAME+" = ?");
        for (int i = 1; i < songs.length; i++) {
            query.append(" AND ").append(COLUMN_SONG_NAME).append(" = ?");
        }
        Cursor cursor = db.rawQuery(query.toString(), songs);
        return cursor;
    }

    public Cursor getNonPlaylistSongs(String list) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] songs = list.split(";");
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SONG_NAME + " NOT IN (?)");

        for (int i = 1; i < songs.length; i++) {
            query.append(" AND ").append(COLUMN_SONG_NAME).append(" NOT IN (?)");
        }
        Cursor cursor = db.rawQuery(query.toString(), songs);
        return cursor;
    }

    public String deleteSong(String songTitle, String groupName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, COLUMN_SONG_TITLE + " = ? AND " + COLUMN_GROUP_NAME + " = ?", new String[]{songTitle, groupName});

        if (rowsAffected > 0) {
            return "Song Removed";
        } else {
            return "No Song Found";
        }
    }
}