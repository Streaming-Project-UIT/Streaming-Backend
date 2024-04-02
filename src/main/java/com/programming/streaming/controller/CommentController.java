package com.programming.streaming.controller;

import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import com.programming.streaming.repository.CommentRepository;
import com.programming.streaming.service.CommentService;
import com.programming.streaming.entity.AuthUser;
import com.programming.streaming.entity.Comment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;


    @PostMapping("/addComment")
    public ResponseEntity postComment(@RequestBody Comment comment) {
        try {
            comment.setAuthor(comment.getAuthor());
            comment.setDislikeCount(comment.getDislikeCount());
            comment.setLikeCount(comment.getLikeCount());
            comment.setText(comment.getText());
            Comment save = commentRepository.save(comment);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/listComment")
    public ResponseEntity getComment() {
        try {
            return ResponseEntity.ok(commentRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
