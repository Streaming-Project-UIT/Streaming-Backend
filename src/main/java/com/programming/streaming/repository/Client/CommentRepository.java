package com.programming.streaming.repository.Client;

import org.springframework.stereotype.Repository;

import com.programming.streaming.model.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List <Comment> findAllByVideoId(String videoId);
}
