package com.example.soundconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelperPlaylists extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "soundConnect.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "PLAYLISTS";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_PLAYLIST_NAME = "PLAYLIST_NAME";
    private static final String COLUMN_DATE_CREATED = "DATE_CREATED";
    private static final String COLUMN_SONG_LIST = "SONG_LIST";

    public DataBaseHelperPlaylists(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DataBaseHelperPlaylists", "DataBaseHelperPlaylists");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLAYLIST_NAME + " TEXT, "
                + COLUMN_SONG_LIST + " TEXT, "
                + COLUMN_DATE_CREATED + " TEXT);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String insertPlaylist(PlaylistModel pm) {
        Log.d("insertPlaylist", "insertPlaylist");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PLAYLIST_NAME, pm.getPlaylistName());
        cv.put(COLUMN_SONG_LIST, pm.getSongList());
        cv.put(COLUMN_DATE_CREATED, pm.getDateCreated());

        String query = "Select " + COLUMN_PLAYLIST_NAME + " from " + TABLE_NAME + " where " + COLUMN_PLAYLIST_NAME + " = '" + pm.getPlaylistName() + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            Log.i("cv", cv.toString());
            try {
                Long insert = db.insert(TABLE_NAME, null, cv);
            } catch (android.database.SQLException e) {
                return "Error adding playlist " + e.getMessage();
            }
            return "Playlist added";
        }
        return "The playlist already exists";
    }

    public Cursor getAllPlaylists() {
        Log.d("getAllPlaylists", "start");
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Log.d("getAllPlaylists", this.getReadableDatabase() + "");
            if (db != null) {
                Log.d("getAllPlaylists", "not null");
                String query = "SELECT * FROM " + TABLE_NAME;
                cursor = db.rawQuery(query, null);
                Log.d("getAllPlaylists", (cursor.moveToFirst()) + "");
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
            Log.d("getAllPlaylists", "cursor = " + cursor);
        } catch (Exception e) {
            Log.d("getReadableDatabase", e.getMessage());
        }
        return cursor;
    }

    public String deletePlaylist(String playlistName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, COLUMN_PLAYLIST_NAME + " = ? ", new String[]{playlistName});

        if (rowsAffected > 0) {
            return "Playlist Removed";
        } else {
            return "Playlist Not Found";
        }
    }

    public String updatePlaylist(PlaylistModel pm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PLAYLIST_NAME, pm.getPlaylistName());
        cv.put(COLUMN_SONG_LIST, pm.getSongList());
        cv.put(COLUMN_DATE_CREATED, pm.getDateCreated());
        String[] args = {pm.getPlaylistName()};
        Log.i("cv", cv.toString());

        if (db.update(TABLE_NAME, cv, COLUMN_PLAYLIST_NAME + "=?", args) > 0) {
            return "Playlist updated";
        }
        return "The playlist doesn't exist";
    }

}