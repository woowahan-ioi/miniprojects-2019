package com.wootube.ioi.service.util;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3FileUploader implements FileUploader {
	private static final String DIRECTORY_NAME = "wootube";

	private final AmazonS3 amazonS3;
	private final String bucket;

	public S3FileUploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
		this.amazonS3 = amazonS3;
		this.bucket = bucket;
	}

	public String uploadCloud(File uploadFile, UploadType uploadType) {
		String fileName = basePath(uploadType) + uploadFile.getName();
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3.getUrl(bucket, fileName).toString();
	}

	private String basePath(UploadType uploadType) {
		return DIRECTORY_NAME + "/" + uploadType.getUploadType() + "/";
	}

	public void deleteFile(String originFileName, UploadType uploadType) {
		amazonS3.deleteObject(bucket, basePath(uploadType) + originFileName);
	}
}
