package com.programming.streaming.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.sql.Timestamp;
@Data
@Builder
@Document("user")
public class AuthUser {
    @Id
    private String id;
    @Indexed
    private String username;
    private String password;
    private boolean active;

    private String firstName;
    private String lastName;
    // private String picture;
    private String sub;
    private byte[] avatar;

    private String email;

    private Timestamp timestamp;

}