package com.programming.streaming.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message extends BaseModel {

    private MessageType messageType;

    private String content;
    private String room;
    private String username;

}