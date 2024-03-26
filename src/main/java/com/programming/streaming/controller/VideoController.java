package com.programming.streaming.controller;
import com.programming.streaming.entity.Video;
import com.programming.streaming.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("video")MultipartFile video, 
            @RequestParam("title") String title, @RequestParam("description") String description) throws IOException {
        return new ResponseEntity<>(videoService.addVideo(video, title, description), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        Video video = videoService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(video.getVideoType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + video.getVideoName() + "\"")
                .body(new ByteArrayResource(video.getVideo()));
    }

    

}
