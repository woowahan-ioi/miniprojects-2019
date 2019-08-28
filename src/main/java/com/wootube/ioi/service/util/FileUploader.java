package com.wootube.ioi.service.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;

import com.wootube.ioi.service.exception.FileUploadException;
import org.imgscalr.Scalr;
import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
	int THUMBNAIL_WIDTH = 200;

	String uploadCloud(File uploadFile, UploadType uploadType);

	void deleteFile(String fileName, UploadType videoUploadType, UploadType thumbnailUploadType);

	default List<String> uploadFile(MultipartFile multipartFile, UploadType videoUploadType, UploadType thumbnailUploadType) {
		try {
			File uploadFile = convert(multipartFile)
					.orElseThrow(FileUploadException::new);

			File thumbnailUploadFile = convert(uploadFile)
					.orElseThrow(FileUploadException::new);

			String videoUrl = uploadCloud(uploadFile, videoUploadType);
			String thumbnailUrl = uploadCloud(thumbnailUploadFile, thumbnailUploadType);

			uploadFile.delete();
			thumbnailUploadFile.delete();

			return Arrays.asList(videoUrl, thumbnailUrl);
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

	default Optional<File> convert(File videoFile) {
		String fileName = videoFile.getAbsolutePath();
		String baseName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.lastIndexOf("."));

		double frameNumber = getFrameNumber(videoFile);
		File imgFile = getThumbNailImageFile(fileName, baseName, frameNumber);
		return Optional.of(imgFile);
	}

	default double getFrameNumber(File videoFile) {
		try {
			SeekableByteChannel byteChannel = NIOUtils.readableFileChannel(videoFile);
			MP4Demuxer demuxer = new MP4Demuxer(byteChannel);
			DemuxerTrack track = demuxer.getVideoTrack();
			return track.getMeta().getTotalDuration() / 5.0;
		} catch (IOException e) {
			videoFile.delete();
			throw new FileUploadException();
		}
	}

	default File getThumbNailImageFile(String fileName, String baseName, double frameNumber) {
		try {
			Picture thumbnail = FrameGrab.getNativeFrame(new File(fileName), frameNumber);
			BufferedImage img = AWTUtil.toBufferedImage(thumbnail);

			File thumbnailFile = new File(baseName + ".png");
			if (!thumbnailFile.exists()) {
				thumbnailFile.createNewFile();
			}

			BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, THUMBNAIL_WIDTH, Scalr.OP_ANTIALIAS);
			ImageIO.write(thumbImg, "png", thumbnailFile);
			return thumbnailFile;
		} catch (IOException | JCodecException e) {
			throw new FileUploadException();
		}
	}
}
