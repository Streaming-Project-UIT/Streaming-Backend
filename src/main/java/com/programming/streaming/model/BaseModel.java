package com.programming.streaming.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;


@Data
@Document
public abstract class BaseModel {

    @Id
    private String id;

    @CreatedDate
    private Date createdDateTime;

}