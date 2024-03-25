package com.programming.streaming.repository;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.programming.streaming.entity.Video;
import java.util.Optional;
import java.util.List;
@Repository
public interface VideoRepository extends MongoRepository<Video, String>{
    Optional<Video> findByTitle(String title);
    List<Video> findAll();
} 
