package com.programming.streaming.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.programming.streaming.model.Video;
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
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    @Autowired
    private MongoTemplate mongoTemplate;

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
        template.store(new ByteArrayInputStream(thumbnail), upload.getOriginalFilename() + "_thumbnail", "image/png",
                thumbnailMetadata);

        return videoID.toString();
    }

    public List<String> getAllVideoIDs() {
        Query query = new Query();
        return template.find(query).map(GridFSFile::getObjectId).map(ObjectId::toString).into(new ArrayList<>());
    }

    public List<String> listIdThumbnail() {
        Query query = Query.query(Criteria.where("metadata._contentType").is("image/png"));
        return template.find(query).map(GridFSFile::getObjectId).map(ObjectId::toString).into(new ArrayList<>());
    }

    public List<String> getThumbnailIdByUserId(String userId) {
        Query query = Query.query(Criteria.where("metadata._contentType").is("image/png"));
        query = query.addCriteria(Criteria.where("metadata.userID").is(userId));
        return template.find(query).map(GridFSFile::getObjectId).map(ObjectId::toString).into(new ArrayList<>());
    }

    public String getVideoIdFromThumbnailId(String thumbnailId) {
        Query query = Query.query(Criteria.where("_id").is(thumbnailId));
        GridFSFile gridFSFile = template.findOne(query);
        return gridFSFile.getMetadata().get("videoId").toString();
    }

    public VideoWithStream getVideoWithStream(String id) throws IOException {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));
        if (gridFSFile != null) {
            return new VideoWithStream(gridFSFile, operations.getResource(gridFSFile).getInputStream());
        }
        return null;
    }

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

    public static class VideoWithStream {
        private GridFSFile gridFSFile;
        private InputStream inputStream;

        public VideoWithStream(GridFSFile gridFSFile, InputStream inputStream) {
            this.gridFSFile = gridFSFile;
            this.inputStream = inputStream;
        }

        public GridFSFile getGridFSFile() {
            return gridFSFile;
        }

        public void setGridFSFile(GridFSFile gridFSFile) {
            this.gridFSFile = gridFSFile;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
    }
    public Video getDetails(String videoId) {
        Query query = new Query(Criteria.where("_id").is(videoId));
        return mongoTemplate.findOne(query, Video.class, "fs.files");
    }
}
