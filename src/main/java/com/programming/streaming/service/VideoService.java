package com.programming.streaming.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.programming.streaming.model.Video;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

@Service
public class VideoService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String addVideo(MultipartFile upload, String userID, byte[] thumbnail, Timestamp timestamp)
            throws IOException {
        DBObject videoMetadata = new BasicDBObject();
        videoMetadata.put("fileSize", upload.getSize());
        videoMetadata.put("userID", userID);
        videoMetadata.put("videoId", new ObjectId().toString());
        videoMetadata.put("timestamp", timestamp.toString());
        Object videoID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
                videoMetadata);

        DBObject thumbnailMetadata = new BasicDBObject();
        thumbnailMetadata.put("fileSize", thumbnail.length);
        thumbnailMetadata.put("userID", userID);
        thumbnailMetadata.put("videoId", videoID.toString());
        thumbnailMetadata.put("timestamp", timestamp.toString());
        Object thumbnailID = template.store(new ByteArrayInputStream(thumbnail),
                upload.getOriginalFilename() + "_thumbnail", "image/png",
                thumbnailMetadata);

        return videoID.toString();
    }

    public List<String> getAllVideoIDs() {
        Query query = new Query(); // Tạo một truy vấn mới
        return template.find(query)
                .map(GridFSFile::getObjectId)
                .map(ObjectId::toString)
                .into(new ArrayList<>());
    }

    public List<String> listIdThumbnail() {
        Query query = Query.query(Criteria.where("metadata._contentType").is("image/png"));
        return template.find(query)
                .map(GridFSFile::getObjectId)
                .map(ObjectId::toString)
                .into(new ArrayList<>());
    }


    public String getVideoIdFromThumbnailId(String thumbnailId) {
        Query query = Query.query(Criteria.where("_id").is(thumbnailId));
        GridFSFile gridFSFile = template.findOne(query);
        return gridFSFile.getMetadata().get("videoId").toString();
    }

    public Video getVideo(String id) throws IOException {

        // search file
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        // convert uri to byteArray
        // save data to LoadFile class
        Video loadFile = new Video();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename(gridFSFile.getFilename());

            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());

            loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return loadFile;
    }

   

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateViews(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("views", 1);
        mongoTemplate.updateFirst(query, update, "fs.files");
    }

    public void updateLikes(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("likes", 1);
        mongoTemplate.updateFirst(query, update, "fs.files");
    }

    public void updateDislikes(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("dislikes", 1);
        mongoTemplate.updateFirst(query, update, "fs.files");
    }
    
}