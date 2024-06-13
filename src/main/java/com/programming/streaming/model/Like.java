package com.programming.streaming.model;

public class Like {
    private String likerToId;
    private String likedToId;

    public Like() {
    }

    public Like(String likerToId, String likedToId) {
        this.likerToId = likerToId;
        this.likedToId = likedToId;
    }

    public String getLikerToId() {
        return likerToId;
    }

    public void setLikerToId(String likerToId) {
        this.likerToId = likerToId;
    }

    public String getLikedToId() {
        return likedToId;
    }

    public void setLikedToId(String likedToId) {
        this.likedToId = likedToId;
    }
    
}
