package com.programming.streaming.model;

public class History {
    private String userId;
    private String thumbId;

    public History() {
    }

    public History(String userId, String thumbId) {
        this.userId = userId;
        this.thumbId = thumbId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThumbId() {
        return thumbId;
    }

    public void setThumbId(String thumbId) {
        this.thumbId = thumbId;
    }
    
}
