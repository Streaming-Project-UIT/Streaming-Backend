package com.programming.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programming.streaming.entity.AuthUser;
import com.programming.streaming.repository.CommentRepository;
import lombok.AllArgsConstructor;
import com.programming.streaming.entity.Comment;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    
    @PostMapping("/upload")
        public ResponseEntity uploadComment(@RequestBody Comment comment) {
            try {
                Comment save = commentRepository.save(comment);
                return ResponseEntity.ok(HttpStatus.CREATED);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(e.getMessage());
            }
    }
}
