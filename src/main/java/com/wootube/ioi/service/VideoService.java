package com.wootube.ioi.service;

import java.util.List;
import javax.transaction.Transactional;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.service.exception.NotFoundVideoIdException;
import com.wootube.ioi.service.exception.NotMatchUserIdException;
import com.wootube.ioi.service.exception.UserAndWriterMisMatchException;
import com.wootube.ioi.service.util.FileUploader;
import com.wootube.ioi.service.util.UploadType;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

	private final FileUploader fileUploader;
	private final ModelMapper modelMapper;

	private final VideoRepository videoRepository;
	private final UserService userService;

	public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository, UserService userService) {
		this.fileUploader = fileUploader;
		this.modelMapper = modelMapper;
		this.videoRepository = videoRepository;
		this.userService = userService;
	}

	public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
		String videoUrl = fileUploader.uploadFile(uploadFile, UploadType.VIDEO);
		String originFileName = uploadFile.getOriginalFilename();

		Video video = modelMapper.map(videoRequestDto, Video.class);
		User writer = userService.findByIdAndIsActiveTrue(videoRequestDto.getWriterId());

		video.initialize(videoUrl, originFileName, writer);
		return modelMapper.map(videoRepository.save(video), VideoResponseDto.class);
	}

	@Transactional
	public VideoResponseDto findVideo(Long id) {
		Video video = findById(id);
		increaseViews(video);
		return modelMapper.map(video, VideoResponseDto.class);
	}

	private void increaseViews(Video video) {
		video.increaseViews();
	}

	public Video findById(Long id) {
		return videoRepository.findById(id)
				.orElseThrow(NotFoundVideoIdException::new);
	}

	@Transactional
	public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
		Video video = findById(id);
		matchWriter(videoRequestDto.getWriterId(), id);

		if (!uploadFile.isEmpty()) {
			fileUploader.deleteFile(video.getOriginFileName(), UploadType.VIDEO);
			String videoUrl = fileUploader.uploadFile(uploadFile, UploadType.VIDEO);
			video.updateContentPath(videoUrl);
		}
		video.update(modelMapper.map(videoRequestDto, Video.class));
		videoRepository.save(video);
	}

	@Transactional
	public void deleteById(Long videoId, Long userId) {
		Video video = findById(videoId);
		if (!video.matchWriter(userId)) {
			throw new UserAndWriterMisMatchException();
		}
		fileUploader.deleteFile(video.getOriginFileName(), UploadType.VIDEO);
		videoRepository.deleteById(video.getId());
	}

	public List<Video> findAllByWriter(Long writerId) {
		User writer = userService.findByIdAndIsActiveTrue(writerId);
		return videoRepository.findByWriter(writer);
	}

	public void matchWriter(Long userId, Long videoId) {
		Video video = findById(videoId);
		if (!video.matchWriter(userId)) {
			throw new NotMatchUserIdException();
		}
	}
}