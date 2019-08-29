package com.wootube.ioi.service.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class S3FileUploader implements FileUploader {
    private static final String DIRECTORY_NAME = "wootube";

    private final AmazonS3 amazonS3Client;
    private final String bucket;

    @Autowired
    public S3FileUploader(AmazonS3 amazonS3,
                          @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3Client = amazonS3;
        this.bucket = bucket;
    }

    @Override
    public String uploadFile(File file, UploadType uploadType) {
        return uploadCloud(file, uploadType);
    }

    @Override
    public void deleteFile(String fileName, UploadType uploadType) {
        amazonS3Client.deleteObject(bucket, basePath(uploadType) + fileName);
    }

    private String uploadCloud(File uploadFile, UploadType uploadType) {
        String fileName = basePath(uploadType) + uploadFile.getName();
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String basePath(UploadType uploadType) {
        return DIRECTORY_NAME + "/" + uploadType.getUploadType() + "/";
    }
}
