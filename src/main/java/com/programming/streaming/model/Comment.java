package com.programming.streaming.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document("comment")
public class Comment {
    @Id
    private String id;

    @Indexed
    private String text;
    private String author;
    private String userName;
    private String videoId;
    private String userId;
    private int number;
    private int likes;
    private int dislikes;
    private boolean isRely;
}
