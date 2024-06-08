package com.programming.streaming.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.programming.streaming.model.Like;
import com.programming.streaming.model.Subscription;
import com.programming.streaming.model.Video;
import com.programming.streaming.repository.Client.LikeRepository;
import com.programming.streaming.repository.Client.SubscriptionRepository;

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
import java.util.Map;

@Service
public class VideoService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    @Autowired
    private MongoTemplate mongoTemplate;

    public String addVideo(MultipartFile upload, String userID, byte[] thumbnail, Timestamp timestamp, String description, String userName, String videoName)
            throws IOException {
        DBObject videoMetadata = new BasicDBObject();
        videoMetadata.put("fileSize", upload.getSize());
        videoMetadata.put("userID", userID);
        videoMetadata.put("videoId", new ObjectId().toString());
        videoMetadata.put("timestamp", timestamp.toString());
        videoMetadata.put("description", description);
        videoMetadata.put("userName", userName);
        videoMetadata.put("videoName", videoName);
        Object videoID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
                videoMetadata);

        DBObject thumbnailMetadata = new BasicDBObject();
        thumbnailMetadata.put("fileSize", thumbnail.length);
        thumbnailMetadata.put("userID", userID);
        thumbnailMetadata.put("videoId", videoID.toString());
        thumbnailMetadata.put("timestamp", timestamp.toString());
        thumbnailMetadata.put("description", description);
        thumbnailMetadata.put("userName", userName);
        thumbnailMetadata.put("videoName", videoName);
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
    
    public ArrayList<String> getDetailsByUserId(String userId) {
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

    public Map<String, Object> getDetails(String videoId) {
        Query query = new Query(Criteria.where("_id").is(videoId));
        DBObject dbObject = mongoTemplate.findOne(query, DBObject.class, "fs.files");
        if (dbObject != null) {
            return dbObject.toMap();
        }
        return null;
    }





    // Handle Subcribe
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public void subscribe(String subscriberId, String subscribedToId) {
        Subscription subscription = new Subscription(subscriberId, subscribedToId);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(String subscriberId, String subscribedToId) {
        subscriptionRepository.deleteBySubscriberIdAndSubscribedToId(subscriberId, subscribedToId);
    }

    public boolean isSubscribed(String subscriberId, String subscribedToId) {
        return subscriptionRepository.existsBySubscriberIdAndSubscribedToId(subscriberId, subscribedToId);
    }
    
    public long getSubscriberCount(String userId) {
        return subscriptionRepository.countBySubscribedToId(userId);
    }

    // Handle Like
    @Autowired
    private LikeRepository likeRepository;

    public void like(String likerToId, String likedToId) {
        Like like = new Like(likerToId, likedToId);
        likeRepository.save(like);
    }
    
    public void unlike(String likerToId, String likedToId) {
        likeRepository.deleteByLikerToIdAndLikedToId(likerToId, likedToId);
    }

    public boolean isLiked(String likerToId, String likedToId) {
        return likeRepository.existsByLikerToIdAndLikedToId(likerToId, likedToId);
    }

    public long getLikeCount(String videoId) {
        return likeRepository.countByLikedToId(videoId);
    }


    public List<String> getLikedToIdsFromLikerToId(String likerToId) {
        Query query = Query.query(Criteria.where("likerToId").is(likerToId));
        List<Like> likes = mongoTemplate.find(query, Like.class);

        List<String> likedToIds = new ArrayList<>();
        for (Like like : likes) {
            likedToIds.add(like.getLikedToId());
        }

        return likedToIds;
    }
    
}
