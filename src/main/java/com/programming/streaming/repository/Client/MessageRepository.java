package com.programming.streaming.repository.Client;

import org.springframework.stereotype.Repository;
import com.programming.streaming.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
    
@Repository
public interface MessageRepository extends MongoRepository<Message, String>{
    List<Message> findAllByRoom(String room);
}

