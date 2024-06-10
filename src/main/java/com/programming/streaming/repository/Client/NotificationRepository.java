package com.programming.streaming.repository.Client;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.programming.streaming.model.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);

}