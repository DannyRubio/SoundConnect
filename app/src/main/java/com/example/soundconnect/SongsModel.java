package com.example.soundconnect;

public class SongsModel {
    private int id;
    private String songTitle;
    private String groupName;
    private String filePath;
    private String dateUploaded;

    public SongsModel(int id, String name, String group, String filePath, String dateAdded) {
        setId(id);
        setSongTitle(name);
        setGroupName(group);
        setFilePath(filePath);
        setDateUploaded(dateAdded);
    }

    public SongsModel() {
    }

    @Override
    public String toString() {
        return "SongsModel{" +
                "id=" + getId() +
                ", title='" + getSongTitle() + '\'' +
                ", group='" + getGroupName() + '\'' +
                ", filePath='" + getFilePath() + '\'' +
                ", dateAdded=" + getDateUploaded() +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
