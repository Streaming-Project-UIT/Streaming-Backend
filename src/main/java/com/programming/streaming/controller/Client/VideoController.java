package com.programming.streaming.controller.Client;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.programming.streaming.service.VideoService;
import com.programming.streaming.service.VideoService.VideoWithStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

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
    public ResponseEntity<?> download(@PathVariable String id,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader) throws IOException {
        VideoWithStream videoWithStream = videoService.getVideoWithStream(id);
        if (videoWithStream == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GridFSFile gridFSFile = videoWithStream.getGridFSFile();
        InputStream inputStream = videoWithStream.getInputStream();

        long fileSize = gridFSFile.getLength();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(gridFSFile.getMetadata().get("_contentType").toString()));
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(UriUtils.encodePath(gridFSFile.getFilename(), StandardCharsets.UTF_8))
                .build());

        if (rangeHeader == null) {
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileSize)
                    .body(new InputStreamResource(inputStream));
        }

        String[] ranges = rangeHeader.replace("bytes=", "").split("-");
        long rangeStart = Long.parseLong(ranges[0]);
        long rangeEnd = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileSize - 1;
        if (rangeEnd > fileSize - 1) {
            rangeEnd = fileSize - 1;
        }
        long rangeLength = rangeEnd - rangeStart + 1;

        inputStream.skip(rangeStart);
        InputStreamResource inputStreamResource = new InputStreamResource(
                new LimitedInputStream(inputStream, rangeLength));

        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);
        headers.setContentLength(rangeLength);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .body(inputStreamResource);
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

    private static class LimitedInputStream extends InputStream {
        private final InputStream in;
        private long remaining;

        public LimitedInputStream(InputStream in, long limit) {
            this.in = in;
            this.remaining = limit;
        }

        @Override
        public int read() throws IOException {
            if (remaining <= 0) {
                return -1;
            }
            int result = in.read();
            if (result != -1) {
                remaining--;
            }
            return result;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining <= 0) {
                return -1;
            }
            int toRead = (int) Math.min(len, remaining);
            int result = in.read(b, off, toRead);
            if (result != -1) {
                remaining -= result;
            }
            return result;
        }
    }
}
