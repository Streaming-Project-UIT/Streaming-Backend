package com.programming.streaming.model;

public class Notification {
    private String userId;
    private String message;
    private boolean status;
    
    public Notification() {
    }

    public Notification(String userId, String message, boolean status) {
        this.userId = userId;
        this.message = message;
        this.status = status;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

}
