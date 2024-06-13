package com.programming.streaming.repository.Client;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.programming.streaming.model.Report;
public interface ReportRepository extends MongoRepository<Report, String> {  
} 