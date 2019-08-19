package com.wootube.ioi.service.util;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3FileUploader implements FileUploader {
	static final String DIRECTORY_NAME = "wootube";

	@Qualifier(value = "amazonS3Client")
	final AmazonS3 amazonS3Client;

	public S3FileUploader(AmazonS3 amazonS3Client) {
		this.amazonS3Client = amazonS3Client;
	}

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadCloud(File uploadFile) {
		String fileName = DIRECTORY_NAME + "/" + uploadFile.getName();
		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	public void deleteFile(String originFileName) {
		amazonS3Client.deleteObject(bucket, DIRECTORY_NAME + "/" + originFileName);
	}
}
