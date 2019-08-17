package com.wootube.ioi.service.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wootube.ioi.service.exception.FileUploadException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FileUploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile, String directoryName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(FileUploadException::new);
        return upload(uploadFile, directoryName);
    }

    private String upload(File uploadFile, String directoryName) {
        String fileName = directoryName + "/" + uploadFile.getName();
        String videoUrl = uploadCloud(uploadFile, fileName);
        uploadFile.delete();
        return videoUrl;
    }

    private String uploadCloud(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public void deleteFile(String directoryName, String originFileName) {
        amazonS3Client.deleteObject(bucket, directoryName + "/" + originFileName);
    }
}
