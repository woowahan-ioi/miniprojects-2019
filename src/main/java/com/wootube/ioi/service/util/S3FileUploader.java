package com.wootube.ioi.service.util;

import java.io.File;
import java.util.List;

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

	//tiber = com.amazonaws.services.s3.AmazonS3Client@3cdc5155
	//생성자com.amazonaws.services.s3.AmazonS3Client@5ae16aa
	public S3FileUploader(List<AmazonS3> amazonS3,
						  @Value("${cloud.aws.s3.bucket}") String bucket) {
		System.out.println("생성자"+ amazonS3);
		for (final AmazonS3 s3 : amazonS3) {
			final String name = s3.getClass().getName();
			System.out.println("s3 = " + s3);
			System.out.println("name = " + name);
		}
		this.amazonS3 = amazonS3.get(0);
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
