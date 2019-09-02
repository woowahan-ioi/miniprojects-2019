package com.wootube.ioi.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;

import com.wootube.ioi.domain.model.S3UploadFileFactory;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.SubscriberResponseDto;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.service.exception.NotFoundVideoIdException;
import com.wootube.ioi.service.exception.NotMatchUserIdException;
import com.wootube.ioi.service.exception.UserAndWriterMisMatchException;
import com.wootube.ioi.service.util.FileConverter;
import com.wootube.ioi.service.util.FileUploader;
import com.wootube.ioi.service.util.UploadType;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.util.stream.Collectors.toList;

@Service
public class VideoService {
	private final FileUploader fileUploader;
	private final ModelMapper modelMapper;
	private final VideoRepository videoRepository;
	private final UserService userService;
	private final FileConverter fileConverter;
	private final SubscriptionService subscriptionService;

	@Autowired
	public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository, UserService userService, FileConverter fileConverter, SubscriptionService subscriptionService) {
		this.fileUploader = fileUploader;
		this.modelMapper = modelMapper;
		this.videoRepository = videoRepository;
		this.userService = userService;
		this.fileConverter = fileConverter;
		this.subscriptionService = subscriptionService;
	}

	public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto, Long writerId) throws IOException {
		S3UploadFileFactory s3UploadFileFactory = new S3UploadFileFactory(uploadFile, fileConverter, fileUploader).invoke();

		User writer = userService.findByIdAndIsActiveTrue(writerId);
		Video video = modelMapper.map(videoRequestDto, Video.class);

		video.initialize(s3UploadFileFactory.getVideoUrl(), s3UploadFileFactory.getThumbnailUrl(),
				s3UploadFileFactory.getOriginFileName(), s3UploadFileFactory.getThumbnailFileName(), writer);
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
	public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto, Long writerId) throws IOException {
		Video video = findById(id);
		matchWriter(writerId, id);

		if (!uploadFile.isEmpty()) {
			fileUploader.deleteFile(video.getOriginFileName(), UploadType.VIDEO);
			fileUploader.deleteFile(video.getThumbnailFileName(), UploadType.THUMBNAIL);

			S3UploadFileFactory s3UploadFileFactory = new S3UploadFileFactory(uploadFile, fileConverter, fileUploader).invoke();

			video.updateVideo(s3UploadFileFactory.getVideoUrl(), s3UploadFileFactory.getOriginFileName(),
					s3UploadFileFactory.getThumbnailUrl(), s3UploadFileFactory.getThumbnailFileName());
		}

		video.updateTitle(videoRequestDto.getTitle());
		video.updateDescription(videoRequestDto.getDescription());
	}

	@Transactional
	public void deleteById(Long videoId, Long userId) {
		Video video = findById(videoId);
		if (!video.matchWriter(userId)) {
			throw new UserAndWriterMisMatchException();
		}
		fileUploader.deleteFile(video.getOriginFileName(), UploadType.VIDEO);
		fileUploader.deleteFile(video.getThumbnailFileName(), UploadType.THUMBNAIL);
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

	public List<VideoResponseDto> findTop20ByOrderByViewsDesc() {
		return videoRepository.findTop20ByOrderByViewsDesc().stream()
				.map(video -> modelMapper.map(video, VideoResponseDto.class))
				.collect(toList());
	}

	public List<VideoResponseDto> findLatestVideos() {
		return videoRepository.findTop12ByOrderByCreateTimeDesc().stream()
				.map(video -> modelMapper.map(video, VideoResponseDto.class))
				.collect(toList());
	}

	public List<VideoResponseDto> findSubscribeVideos(Long userId) {
		List<SubscriberResponseDto> subscriptions = subscriptionService.findAllUsersBySubscriberId(userId);
		Set<Video> subscribedVideos = new HashSet<>();

		for (int i = 0; i < subscriptions.size(); i++) {
			User writer = userService.findByIdAndIsActiveTrue(subscriptions.get(i).getId());
			videoRepository.findByWriter(writer)
					.forEach(video -> subscribedVideos.add(video));
		}

		return subscribedVideos.stream()
				.map(video -> modelMapper.map(video, VideoResponseDto.class))
				.limit(12)
				.collect(toList());
	}

	public List<VideoResponseDto> findRecommendVideos() {
		return videoRepository.findAll().stream()
				.limit(12)
				.map(video -> modelMapper.map(video, VideoResponseDto.class))
				.collect(toList());
	}

	public List<VideoResponseDto> findPopularityVideos() {
		return videoRepository.findTop12ByOrderByViewsDesc().stream()
				.map(video -> modelMapper.map(video, VideoResponseDto.class))
				.collect(toList());
	}
}