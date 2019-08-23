package com.wootube.ioi.service;

import java.util.List;
import javax.transaction.Transactional;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.service.exception.NotFoundUserException;
import com.wootube.ioi.service.exception.NotFoundVideoIdException;
import com.wootube.ioi.service.exception.NotMatchUserIdException;
import com.wootube.ioi.service.exception.UserAndWriterMisMatchException;
import com.wootube.ioi.service.util.FileUploader;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {
	private final FileUploader fileUploader;
	private final ModelMapper modelMapper;

	private final VideoRepository videoRepository;
	private final UserRepository userRepository;

	public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository, UserRepository userRepository) {
		this.fileUploader = fileUploader;
		this.modelMapper = modelMapper;
		this.videoRepository = videoRepository;
		this.userRepository = userRepository;
	}

	public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
		String videoUrl = fileUploader.uploadFile(uploadFile);
		String originFileName = uploadFile.getOriginalFilename();

		Video video = modelMapper.map(videoRequestDto, Video.class);
		User writer = findUser(videoRequestDto.getUserId());

		video.initialize(videoUrl, originFileName, writer);
		return modelMapper.map(videoRepository.save(video), VideoResponseDto.class);
	}

	private User findUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
	}

	public VideoResponseDto findById(Long id) {
		return modelMapper.map(findVideo(id), VideoResponseDto.class);
	}

	public Video findVideo(Long id) {
		return videoRepository.findById(id)
				.orElseThrow(NotFoundVideoIdException::new);
	}

	@Transactional
	public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
		Video video = findVideo(id);

		matchWriter(videoRequestDto.getUserId(), id);

		if (!uploadFile.isEmpty()) {
			fileUploader.deleteFile(video.getOriginFileName());
			String videoUrl = fileUploader.uploadFile(uploadFile);
			video.updateContentPath(videoUrl);
		}
		video.update(modelMapper.map(videoRequestDto, Video.class));
		videoRepository.save(video);
	}

	@Transactional
	public void deleteById(Long videoId, Long userId) {
		Video video = findVideo(videoId);

		if(!video.matchWriter(userId)) {
			throw new UserAndWriterMisMatchException();
		}

		fileUploader.deleteFile(video.getOriginFileName());
		videoRepository.deleteById(video.getId());
	}

	public List<Video> findVideosByWriter(Long writerId) {
		User writer = findUser(writerId);
		return videoRepository.findByWriter(writer);
	}

	public void matchWriter(Long userId, Long videoId) {
		Video video = findVideo(videoId);
		if(!video.matchWriter(userId)) {
			throw new NotMatchUserIdException();
		}
	}
}
