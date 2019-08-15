package com.wootube.ioi.service;

import java.io.IOException;
import javax.transaction.Transactional;

import com.wootube.ioi.domain.model.video.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.exception.FileUploadException;
import com.wootube.ioi.service.exception.NotFoundVideoException;
import com.wootube.ioi.service.util.FileUploader;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {
	private final String directoryName = "wootube";
	private final FileUploader fileUploader;
	private final ModelMapper modelMapper;

	private final VideoRepository videoRepository;

	public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository) {
		this.fileUploader = fileUploader;
		this.modelMapper = modelMapper;
		this.videoRepository = videoRepository;
	}

	public VideoRequestDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
		String videoUrl = uploadFile(uploadFile, directoryName);
		String originFileName = uploadFile.getOriginalFilename();
		Video video = modelMapper.map(videoRequestDto, Video.class);

		video.setContentPath(videoUrl);
		video.setOriginFileName(originFileName);

		return modelMapper.map(videoRepository.save(video), VideoRequestDto.class);
	}

	public VideoRequestDto findById(Long id) {
		return modelMapper.map(findVideo(id), VideoRequestDto.class);
	}

	private Video findVideo(Long id) {
		return videoRepository.findById(id)
				.orElseThrow(NotFoundVideoException::new);
	}

	@Transactional
	public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
		Video video = findVideo(id);
		if (!uploadFile.isEmpty()) {
			fileUploader.deleteFile(directoryName, video.getOriginFileName());
			String videoUrl = uploadFile(uploadFile, directoryName);
			video.setContentPath(videoUrl);
		}
		video.update(modelMapper.map(videoRequestDto, Video.class));
		videoRepository.save(video);
	}

	public String uploadFile(MultipartFile uploadFile, String directoryName) {
		try {
			return fileUploader.uploadFile(uploadFile, directoryName);
		} catch (IOException e) {
			throw new FileUploadException();
		}
	}
}
