package com.programming.streaming.model;

public class Report {
    private String videoId;
    private String msg;
    private String userId;

    public Report() {

    }

    public Report(String videoId, String msg, String userId) {
        this.videoId = videoId;
        this.msg = msg;
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getMsg() {
        return msg;
    }

    public String getUserId() {
        return userId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    } 
    
}
