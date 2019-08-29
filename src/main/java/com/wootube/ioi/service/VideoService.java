package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.service.exception.FileConvertException;
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

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class VideoService {
    private final FileUploader fileUploader;
    private final ModelMapper modelMapper;
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final FileConverter fileConverter;

    @Autowired
    public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository, UserService userService, FileConverter fileConverter) {
        this.fileUploader = fileUploader;
        this.modelMapper = modelMapper;
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.fileConverter = fileConverter;
    }

    public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto) throws IOException {
        File convertedVideo = fileConverter.convert(uploadFile)
                .orElseThrow(FileConvertException::new);

        String videoUrl = fileUploader.uploadFile(convertedVideo, UploadType.VIDEO);

        File convertedThumbnail = fileConverter.convert(convertedVideo)
                .orElseThrow(FileConvertException::new);

        String thumbnailUrl = fileUploader.uploadFile(convertedThumbnail, UploadType.THUMBNAIL);

        String originFileName = convertedVideo.getName();
        String thumbnailFileName = convertedThumbnail.getName();

        convertedVideo.delete();
        convertedThumbnail.delete();

        User writer = userService.findByIdAndIsActiveTrue(videoRequestDto.getWriterId());

        Video video = modelMapper.map(videoRequestDto, Video.class);
        video.initialize(videoUrl, thumbnailUrl, originFileName, thumbnailFileName, writer);
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
    public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) throws IOException {
        Video video = findById(id);
        matchWriter(videoRequestDto.getWriterId(), id);

        if (!uploadFile.isEmpty()) {
            fileUploader.deleteFile(video.getOriginFileName(), UploadType.VIDEO);
            fileUploader.deleteFile(video.getThumbnailFileName(), UploadType.THUMBNAIL);

            File convertedVideo = fileConverter.convert(uploadFile)
                    .orElseThrow(FileConvertException::new);

            String contentPath = fileUploader.uploadFile(convertedVideo, UploadType.VIDEO);

            File convertedThumbnail = fileConverter.convert(convertedVideo)
                    .orElseThrow(FileConvertException::new);

            String thumbnailPath = fileUploader.uploadFile(convertedThumbnail, UploadType.THUMBNAIL);

            String originFileName = convertedVideo.getName();
            String thumbnailFileName = convertedThumbnail.getName();

            convertedVideo.delete();
            convertedThumbnail.delete();

            video.updateVideo(contentPath, originFileName, thumbnailPath, thumbnailFileName);
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

    public List<VideoResponseDto> findAll() {
        return videoRepository.findAll().stream()
                .map(video -> modelMapper.map(video, VideoResponseDto.class))
                .collect(toList());
    }

    public List<VideoResponseDto> findLatestVideos() {
        return videoRepository.findTop12ByOrderByCreateTimeDesc().stream()
                .map(video -> modelMapper.map(video, VideoResponseDto.class))
                .collect(toList());
    }

    public List<VideoResponseDto> findSubscribeVideos() {
        List<Video> findVideos = videoRepository.findAll();
        Collections.shuffle(findVideos);

        return findVideos.stream()
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