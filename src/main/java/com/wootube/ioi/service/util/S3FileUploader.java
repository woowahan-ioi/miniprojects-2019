package com.wootube.ioi.service.util;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3FileUploader implements FileUploader {
    private static final String DIRECTORY_NAME = "wootube";
    private static final String THUMBNAIL_SPLIT_REGEX = "[.]";
    private static final String THUMBNAIL_FILE_EXTENSION = ".png";

    private final AmazonS3 amazonS3Client;
    private final String bucket;

    public S3FileUploader(AmazonS3 amazonS3,
                          @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3Client = amazonS3;
        this.bucket = bucket;
    }

    public String uploadCloud(File uploadFile, UploadType uploadType) {
        String fileName = basePath(uploadType) + uploadFile.getName();
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String basePath(UploadType uploadType) {
        return DIRECTORY_NAME + "/" + uploadType.getUploadType() + "/";
    }

    public void deleteFile(String originFileName, UploadType videoUploadType, UploadType thumbnailUploadType) {
        amazonS3Client.deleteObject(bucket, basePath(videoUploadType) + originFileName);
        amazonS3Client.deleteObject(bucket, basePath(thumbnailUploadType) + splitOriginFileName(originFileName) + THUMBNAIL_FILE_EXTENSION);
    }

    private String splitOriginFileName(String originFileName) {
        String[] separatedFileName = originFileName.split(THUMBNAIL_SPLIT_REGEX);
        int fileNameCount = separatedFileName.length - 1;

        return Arrays.stream(separatedFileName)
                .limit(fileNameCount)
                .collect(Collectors.joining());
    }
}
