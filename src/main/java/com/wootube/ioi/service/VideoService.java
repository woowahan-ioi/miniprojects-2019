package com.wootube.ioi.service;

import java.io.IOException;
import javax.transaction.Transactional;

import com.wootube.ioi.domain.model.video.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.exception.FileUploadException;
import com.wootube.ioi.exception.NotFoundVideoException;
import com.wootube.ioi.request.VideoDto;
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

	public VideoDto create(MultipartFile uploadFile, VideoDto videoDto) {
		String videoUrl = uploadFile(uploadFile, directoryName);
		String originFileName = uploadFile.getOriginalFilename();
		Video video = modelMapper.map(videoDto, Video.class);
		video.setContentPath(videoUrl);
		video.setOriginFileName(originFileName);
		return modelMapper.map(videoRepository.save(video), VideoDto.class);
	}

	public VideoDto findById(Long id) {
		return modelMapper.map(findVideo(id), VideoDto.class);
	}

	private Video findVideo(Long id) {
		return videoRepository.findById(id)
				.orElseThrow(NotFoundVideoException::new);
	}

	@Transactional
	public void update(Long id, MultipartFile uploadFile, VideoDto videoDto) {
		Video video = findVideo(id);
		if(!uploadFile.isEmpty()) {
			fileUploader.deleteFile(directoryName, video.getOriginFileName());
			String videoUrl = uploadFile(uploadFile, directoryName);
			video.setContentPath(videoUrl);
		}
		video.update(modelMapper.map(videoDto, Video.class));
		videoRepository.save(video);
	}

	@Transactional
	public void deleteById(Long id) {
		Video video = findVideo(id);
		fileUploader.deleteFile(directoryName, video.getOriginFileName());
		videoRepository.delete(findVideo(id));
	}

	public String uploadFile(MultipartFile uploadFile, String directoryName) {
		try {
			return fileUploader.uploadFile(uploadFile, directoryName);
		} catch (IOException e) {
			throw new FileUploadException();
		}
	}
}
