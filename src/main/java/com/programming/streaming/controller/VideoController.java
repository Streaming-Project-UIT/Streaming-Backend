package com.programming.streaming.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.programming.streaming.entity.AuthUser;
import com.programming.streaming.entity.Video;
import com.programming.streaming.repository.VideoRepository;
import com.programming.streaming.service.VideoService;

import ch.qos.logback.core.model.Model;
import lombok.AllArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

@RestController
@RequestMapping("/videos")
@AllArgsConstructor
public class VideoController {
    public final VideoRepository videoRepository;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity registerUser(@RequestBody Video video) {
        try {
            
            video.setDescription(video.getDescription());
            Video save = videoRepository.save(video);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity listVideos() {
        try {
            return ResponseEntity.ok(videoRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

