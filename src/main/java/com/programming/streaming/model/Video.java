package com.programming.streaming.model;
public class Video {

    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
    private String description;
    private String userID;
    private byte[] thumbnail;
    // private int views;
    // private int likes;
    // private int dislikes;

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Video() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    // public int getViews() {
    //     return views;
    // }

    // public void setViews(int views) {
    //     this.views = views;
    // }

    // public int getLikes() {
    //     return likes;
    // }

    // public void setLikes(int likes) {
    //     this.likes = likes;
    // }

    // public int getDislikes() {
    //     return dislikes;
    // }
    // public void setDislikes(int dislikes) {
    //     this.dislikes = dislikes;
    // }
}