package com.example.soundconnect;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllSongs extends AppCompatActivity {
    DataBaseHelperSongs db;
    RecyclerView recyclerView;
    ArrayList<String> song_Title, group_Name, file_Path;
    ArrayList<Integer> audio_resource_id;
    AllSongsAdapter allSongsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_songs);

        db = new DataBaseHelperSongs(this);
        song_Title = new ArrayList<>();
        group_Name = new ArrayList<>();
        file_Path = new ArrayList<>();
        audio_resource_id = new ArrayList<>();
        displayData();
        recyclerView = findViewById(R.id.rvSongsList);
        Log.d("song_Title","size: "+song_Title.size());
        allSongsAdapter = new AllSongsAdapter(this, song_Title, group_Name, file_Path);
        recyclerView.setAdapter(allSongsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void displayData() {
        Cursor cursor = db.getAllSongs();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No songs yet", Toast.LENGTH_SHORT).show();
            return;
        }
        while (cursor.moveToNext()) {
            int columnIndex=cursor.getColumnIndex("SONG_TITLE");
            if(columnIndex!=-1) {
                song_Title.add(cursor.getString(columnIndex));
            }

            columnIndex=cursor.getColumnIndex("GROUP_NAME");
            if(columnIndex!=-1) {
                group_Name.add(cursor.getString(columnIndex));
            }

            columnIndex=cursor.getColumnIndex("FILE_PATH");
            Log.d("FilePath",cursor.getString(columnIndex));
            Log.d("FileExists",new File(cursor.getString(columnIndex)).exists()+"");
            if(columnIndex!=-1) {
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

    //Upload songs
    int requestCodeGetSongDirectory = 1;
    int requestCodeModifyNameGroup = 2;
    SongsModel sm = new SongsModel();

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DataBaseHelperSongs dbh = new DataBaseHelperSongs(AllSongs.this);
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getApplicationContext();
        if (requestCode == this.requestCodeGetSongDirectory && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    Log.i("uri gotten", uri.toString());
                    sm.setFilePath(uri.toString());
                    Intent gotoAddNameGroup = new Intent(this, AddSongTitleGroupName.class);
                    startActivityForResult(gotoAddNameGroup, requestCodeModifyNameGroup);
                } catch (Exception e) {
                    Toast.makeText(context, "Error uploading song: " + e.getMessage(), Toast.LENGTH_SHORT * 10).show();
                    setSMtoNull(sm);
                }
            }
        } else if (requestCode == this.requestCodeModifyNameGroup && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = Uri.parse(sm.getFilePath());

                if (DocumentsContract.isDocumentUri(context, uri)) {
                    try {
                        Log.d("URI", "FULL URI " + uri.toString());
                        Log.d("uri.getPath()", uri.getPath());
                        Log.d("uri.getPath()", uri.getPathSegments().toString());
                        String filePath = null;

                        DocumentFile documentFile = DocumentFile.fromSingleUri(context, uri);
                        Log.i("sm.getFilePath()", sm.getFilePath());
                        Log.i("documentFile", documentFile.toString());

                        if (documentFile != null && documentFile.exists()) {
                            Log.i("1st if", "true");
                            if (documentFile.getUri().getScheme().equals("content")) {
                                Log.i("2nd if", "true");

                                String songFileName = data.getStringExtra("songTitle") + " - " + data.getStringExtra("groupName")/*uri.toString().substring(uri.toString().lastIndexOf("."))*/;

                                filePath = MoveAudioFile(this, documentFile.getUri(), songFileName);

                                if (filePath != null) {
                                    File testFile = new File(filePath);
                                    if (!testFile.exists()) {
                                        Log.e("FILE NOT MOVED", "FILE PATH DOESN'T EXIST");
                                    } else {
                                        Log.e("FILE PATH EXISTS", "FILE PATH EXISTS");
                                    }
                                } else {
                                    Log.e("FILE ERROR", "FILE DOESN'T EXIST");
                                }
                            }
                        }
                        if (filePath != null) {
                            sm.setFilePath(filePath);
                        } else {
                            Toast.makeText(context, "Error uploading song: try agin", Toast.LENGTH_SHORT * 10).show();
                            setSMtoNull(sm);
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error uploading song: " + e.getMessage(), Toast.LENGTH_SHORT * 10).show();
                        setSMtoNull(sm);
                    }
                }

                sm.setSongTitle(data.getStringExtra("songTitle"));
                sm.setGroupName(data.getStringExtra("groupName"));
                sm.setDateUploaded(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").format(new Date()));
                String response = dbh.insertSong(sm);

                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(context, "Song info empty, try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("processCanceled","process Canceled");
            Toast.makeText(context, "Error adding song resultCode:" + resultCode + "\n", Toast.LENGTH_SHORT).show();
            setSMtoNull(sm);
        }
    }

    public void uploadSong(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, requestCodeGetSongDirectory);
    }

    public void removeSongs(View view) {
        if (allSongsAdapter.activateRemove()) {
            findViewById(R.id.btnRemoveSongs).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtRemoveSongs).setVisibility(View.INVISIBLE);
            findViewById(R.id.imgMinusRemoveNP).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnRemoveAccept).setVisibility(View.VISIBLE);
            findViewById(R.id.btnRemoveCancel).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.btnRemoveSongs).setVisibility(View.VISIBLE);
            findViewById(R.id.txtRemoveSongs).setVisibility(View.VISIBLE);
            findViewById(R.id.imgMinusRemoveNP).setVisibility(View.VISIBLE);
            findViewById(R.id.btnRemoveAccept).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnRemoveCancel).setVisibility(View.INVISIBLE);
        }
    }

    public void acceptRemove(View view) {
        Context context = getApplicationContext();
        ArrayList<Integer> selectedSongs = allSongsAdapter.getSelectedSongs();
        if (selectedSongs.size() != 0) {
            for (Integer songNum : selectedSongs) {
                if(new File(file_Path.get(songNum)).delete()) {
                    db.deleteSong(song_Title.get(songNum), group_Name.get(songNum));
                }
            }
            String msg;
            if (selectedSongs.size() == 1) {
                msg = "Song removed";
            } else {
                msg = "Songs removed";
            }
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());
        } else {
            //Deactivate buttons
            removeSongs(view);
        }
    }

    private void setSMtoNull(SongsModel sm) {
        sm.setId(-1);
        sm.setSongTitle(null);
        sm.setGroupName(null);
        sm.setFilePath(null);
        sm.setDateUploaded(null);
    }

    public String MoveAudioFile(Context context, Uri contentUri, String newFileName) {
        Log.d("contentUri", contentUri.toString());
        Log.d("newFileName", newFileName);
        String filePath = null,extension="";

        ContentResolver contentResolver = context.getContentResolver();

        // Try to get the file extension from the ContentResolver
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extensionFromMimeType = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(contentUri));

        if (extensionFromMimeType != null) {
            extension="."+extensionFromMimeType;
            Log.d("extensionFromMimeType", extensionFromMimeType);
        } else {
            // If the ContentResolver doesn't provide the extension, try to extract it from the URI
            String uriString = contentUri.toString();
            int lastDotIndex = uriString.lastIndexOf('.');
            if (lastDotIndex != -1) {
                extension="."+ uriString.substring(lastDotIndex + 1);
            }
        }
    newFileName+=extension;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(contentUri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                FileInputStream inputStream = new FileInputStream(fileDescriptor);
                File destinationDirectory = new File(getFilesDir(), "uploadedSongs");

                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs();
                }

                File file = new File(destinationDirectory, newFileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                filePath = file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error moving", "Error moving");
            Log.d("e.printStackTrace()", e.getMessage());
        }
        return filePath;
    }
}
