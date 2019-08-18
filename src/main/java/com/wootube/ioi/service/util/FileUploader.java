package com.wootube.ioi.service.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import com.wootube.ioi.service.exception.FileUploadException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
	String uploadCloud(File uploadFile);

	void deleteFile(String fileName);

	default String uploadFile(MultipartFile multipartFile) {
		try {
			File uploadFile = convert(multipartFile)
					.orElseThrow(FileUploadException::new);
			String videoUrl = uploadCloud(uploadFile);
			uploadFile.delete();
			return videoUrl;
		} catch (IOException e) {
			throw new FileUploadException();
		}
	}

	default Optional<File> convert(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
			}
			return Optional.of(convertFile);
		}
		return Optional.empty();
	}
}
