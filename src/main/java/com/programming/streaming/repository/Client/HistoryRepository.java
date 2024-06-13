package com.programming.streaming.repository.Client;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.programming.streaming.model.History;
public interface HistoryRepository extends MongoRepository<History, String>{

    
} 