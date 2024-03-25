package com.programming.streaming.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Document("video")
@Getter
@Setter
public class Video {
    @Id
    private String id;

    @Indexed
    private String description;
    private String title;
    private String userId;
    private int likes;
    private int dislikes;

    public Video() {
    }

    
    

}
