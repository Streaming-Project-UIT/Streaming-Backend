package com.programming.streaming.model;

public class Subscription {
    private String subscriberId;
    private String subscribedToId;

    public Subscription() {
    }

    public Subscription(String subscriberId, String subscribedToId) {
        this.subscriberId = subscriberId;
        this.subscribedToId = subscribedToId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscribedToId() {
        return subscribedToId;
    }

    public void setSubscribedToId(String subscribedToId) {
        this.subscribedToId = subscribedToId;
    }
}
