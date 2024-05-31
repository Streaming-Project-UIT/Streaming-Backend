package com.programming.streaming.controller.Client;

import com.programming.streaming.model.Video;
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
import java.sql.Timestamp;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @CrossOrigin(origins = "*")
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
            @RequestParam("userID") String userID,
            @RequestParam("thumbnail") MultipartFile thumbnailFile) throws IOException {
        byte[] thumbnail = thumbnailFile.getBytes();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new ResponseEntity<>(videoService.addVideo(file, userID, thumbnail, timestamp), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/get/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        Video loadFile = videoService.getVideo(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }




    @CrossOrigin(origins = "*")
    @GetMapping("/getAllIds")
    public ResponseEntity<?> getAllID() {
        return new ResponseEntity<>(videoService.getAllVideoIDs(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/listIdThumbnail")
    public ResponseEntity<?> listIdThumbnail() {
        return new ResponseEntity<>(videoService.listIdThumbnail(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getVideoIdFromThumbnailId/{id}")
    public ResponseEntity<?> getVideoIdFromThumbnailId(@PathVariable String id) {
        return new ResponseEntity<>(videoService.getVideoIdFromThumbnailId(id), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/updateViews/{id}")
    public ResponseEntity<?> updateViews(@PathVariable String id) {
        videoService.updateViews(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/updateLikes/{id}")
    public ResponseEntity<?> updateLikes(@PathVariable String id) {
        videoService.updateLikes(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/updateDislikes/{id}")
    public ResponseEntity<?> updateDislikes(@PathVariable String id) {
        videoService.updateDislikes(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
}