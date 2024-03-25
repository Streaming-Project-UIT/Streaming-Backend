package com.programming.streaming.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.BsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.programming.streaming.entity.LoadFile;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import org.bson.Document;
@Service
public class FileService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String addFile(MultipartFile upload, String title) throws IOException {
        // , String title, String description
        // define additional metadata
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());
        // metadata.put("fileType", upload.getContentType());

        metadata.put("title", title);
        // metadata.put("description", description);
        // store in database which returns the objectID
        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
                metadata);

        // return as a string
        return fileID.toString();
    }

    
    public List<Map<String, Object>> listFiles() {
        Query query = new Query(); // Tạo một đối tượng Query rỗng
        List<GridFSFile> gridFSFiles = template.find(query).into(new ArrayList<>()); // Tìm tất cả các file trong GridFS
        List<Map<String, Object>> files = new ArrayList<>();
        for (GridFSFile file : gridFSFiles) {
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("filename", file.getFilename());
            fileInfo.put("contentType", file.getMetadata().get("contentType"));
            fileInfo.put("fileSize", file.getLength());
            // Thêm thông tin khác nếu cần
            Document metadata = file.getMetadata();
            for (String key : metadata.keySet()) {
                fileInfo.put(key, metadata.get(key));
            }
            fileInfo.put("title", metadata.get("title"));
            files.add(fileInfo);
        }
        return files;
    }
    

    public LoadFile downloadFile(String id) throws IOException {

        // search file
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        // convert uri to byteArray
        // save data to LoadFile class
        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename(gridFSFile.getFilename());

            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());

            loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());

            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return loadFile;
    }

    public void downloadFilesAsZip(HttpServletResponse response) throws IOException {

        // get all files in db
        List<GridFSFile> fileList = new ArrayList<>();
        template.find(new Query()).into(fileList);

        // if fileList is not empty, loop through the list
        if (fileList.size() > 0) {

            // create a zip file
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

            for (GridFSFile gridFSFile : fileList) {
                // file id is returning as a bson value
                BsonValue bsonValue = gridFSFile.getId();
                String file_id = String.valueOf(bsonValue.asObjectId().getValue());

                // find and retrieve file (using previous download method)
                LoadFile file = downloadFile(file_id);

                // add file to the zip file entry
                ZipEntry zipEntry = new ZipEntry(file.getFilename());
                zipEntry.setSize(Long.parseLong(file.getFileSize()));

                zipOutputStream.putNextEntry(zipEntry);

                ByteArrayResource fileResource = new ByteArrayResource(file.getFile());
                StreamUtils.copy(fileResource.getInputStream(), zipOutputStream);

                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            zipOutputStream.close();
        }

    }
}