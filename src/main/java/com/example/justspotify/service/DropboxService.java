package com.example.justspotify.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class DropboxService {
    
    private static final Logger logger = LoggerFactory.getLogger(DropboxService.class);
    
    @Autowired
    private DbxClientV2 dropboxClient;
    
    public String uploadFile(MultipartFile file, String fileName) throws IOException, DbxException {
        try {
            // Generate unique file name to avoid conflicts
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            String path = "/music/" + uniqueFileName;
            
            logger.info("Attempting to upload file: {} to path: {}", fileName, path);
            
            try (InputStream in = file.getInputStream()) {
                FileMetadata metadata = dropboxClient.files().uploadBuilder(path)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(in);
                
                logger.info("File uploaded successfully: {}", metadata.getName());
                return path;
            }
        } catch (Exception e) {
            logger.error("Failed to upload file to Dropbox: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    public void deleteFile(String filePath) throws DbxException {
        try {
            dropboxClient.files().deleteV2(filePath);
            logger.info("File deleted successfully: {}", filePath);
        } catch (Exception e) {
            logger.error("Failed to delete file from Dropbox: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    public String getTemporaryLink(String filePath) throws DbxException {
        try {
            String link = dropboxClient.files().getTemporaryLink(filePath).getLink();
            logger.info("Generated temporary link for file: {}", filePath);
            return link;
        } catch (Exception e) {
            logger.error("Failed to get temporary link: {}", e.getMessage(), e);
            throw e;
        }
    }
}