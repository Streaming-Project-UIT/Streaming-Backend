package com.programming.streaming.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.programming.streaming.entity.Video;
import com.programming.streaming.repository.VideoRepository;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    // public Video saveVideo(String description, String title, String userId) throws IOException {
    //     video.setDescription(description);
    //     video.setTitle(title);
    //     video.setUserId(userId);
    //     return videoRepository.save(video);
    // }
}