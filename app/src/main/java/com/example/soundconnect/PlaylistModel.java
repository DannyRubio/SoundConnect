package com.example.soundconnect;

public class PlaylistModel {
    private int id;
    private String playlistName;
    private String songList;
    private String dateCreated;

    public PlaylistModel(int id, String playlistName, String songList, String dateCreated) {
        setId(id);
        setPlaylistName(playlistName);
        setSongList(songList);
        setDateCreated(dateCreated);
    }

    public PlaylistModel() {
    }

    @Override
    public String toString() {
        return "PlaylistModel{" +
                "id=" + id +
                ", playlistName='" + playlistName + '\'' +
                ", songList='" + songList + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getSongList() {
        return songList;
    }

    public void setSongList(String songList) {
        this.songList = songList;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}