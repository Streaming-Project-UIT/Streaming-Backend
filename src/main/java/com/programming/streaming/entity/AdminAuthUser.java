package com.programming.streaming.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document("admin_auth_user")
public class AdminAuthUser {
    @Id
    private String id;
    @Indexed
    private String username;
    private String password;
    private Timestamp timestamp;
}
