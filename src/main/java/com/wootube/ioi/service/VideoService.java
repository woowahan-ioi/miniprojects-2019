package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.S3UploadFileFactory;
import com.wootube.ioi.domain.model.Subscription;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.*;

@Service
public class VideoService {
    private final ModelMapper modelMapper;
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final FileConverter fileConverter;
    private final FileUploader fileUploader;

    @Autowired
    public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository, UserService userService, FileConverter fileConverter, SubscriptionService subscriptionService) {
        this.fileUploader = fileUploader;
        this.fileConverter = fileConverter;
        this.modelMapper = modelMapper;
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto, Long writerId) {
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
    public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto, Long writerId) {
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

    public List<VideoResponseDto> findSubscribeVideos(Pageable pageable, Long subscriberId) {
        List<Subscription> subscriptions = subscriptionService.findAllBySubscriberId(subscriberId);
        return subscriptions.stream()
                .map(subscription -> subscription.getSubscribedUser().getId())
                .map(writerId -> videoRepository.findByWriter(userService.findByIdAndIsActiveTrue(writerId)))
                .flatMap(Collection::stream)
                .limit(pageable.getPageSize())
                .map(video -> modelMapper.map(video, VideoResponseDto.class))
                .collect(toList());
    }

    public List<VideoResponseDto> findRecommendVideos(Pageable pageable) {
        return videoRepository.findAllRandom(pageable).stream()
                .map(video -> modelMapper.map(video, VideoResponseDto.class))
                .collect(toList());
    }

    public List<VideoResponseDto> findPopularityVideos(Pageable pageable) {
        return videoRepository.findAll(pageable).stream()
                .map(video -> modelMapper.map(video, VideoResponseDto.class))
                .collect(toList());
    }

    public List<VideoResponseDto> findLatestVideos(Pageable pageable) {
        return videoRepository.findAll(pageable).stream()
                .map(video -> modelMapper.map(video, VideoResponseDto.class))
                .collect(toList());
    }
}