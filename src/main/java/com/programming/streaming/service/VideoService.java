package com.programming.streaming.service;

import com.programming.streaming.entity.Video;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class VideoService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String addVideo(MultipartFile upload, String title, String description) throws IOException {

        // define additional metadata
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());


        metadata.put("title", title);
        metadata.put("description", description);
        // store in database which returns the objectID
        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
                metadata);

        // return as a string
        return fileID.toString();
    }

    public Video downloadFile(String id) throws IOException {

        // search file
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        // convert uri to byteArray
        // save data to LoadFile class
        Video video = new Video();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            video.setVideoName(gridFSFile.getFilename());

            video.setVideoType(gridFSFile.getMetadata().get("_contentType").toString());

            video.setVideoSize(gridFSFile.getMetadata().get("videoSize").toString());

            video.setVideo(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return video;
    }

}
