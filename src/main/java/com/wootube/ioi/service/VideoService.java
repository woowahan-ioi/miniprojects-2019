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
import com.wootube.ioi.service.util.UploadType;
import com.wootube.ioi.service.util.bmoluffy.FileConverter;
import com.wootube.ioi.service.util.bmoluffy.FileUploader2;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class VideoService {
    private final FileUploader2 fileUploader;
    private final ModelMapper modelMapper;
    private final VideoRepository videoRepository;
    private final UserService userService;

    public VideoService(FileUploader2 fileUploader, ModelMapper modelMapper, VideoRepository videoRepository, UserService userService) {
        this.fileUploader = fileUploader;
        this.modelMapper = modelMapper;
        this.videoRepository = videoRepository;
        this.userService = userService;
    }

    public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto) throws IOException {
        File convertedVideo = FileConverter.convert(uploadFile)
                .orElseThrow(FileConvertException::new);

        String videoUrl = fileUploader.uploadFile(convertedVideo, UploadType.VIDEO);

        File convertedThumbnail = FileConverter.convert(convertedVideo)
                .orElseThrow(FileConvertException::new);

        String thumbnailUrl = fileUploader.uploadFile(convertedThumbnail, UploadType.THUMBNAIL);

        String originFileName = uploadFile.getOriginalFilename();
        String thumbnailFileName = convertedThumbnail.getName();

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

            File convertedVideo = FileConverter.convert(uploadFile)
                    .orElseThrow(FileConvertException::new);

            String videoUrl = fileUploader.uploadFile(convertedVideo, UploadType.VIDEO);

            File convertedThumbnail = FileConverter.convert(convertedVideo)
                    .orElseThrow(FileConvertException::new);

            String thumbnailUrl = fileUploader.uploadFile(convertedThumbnail, UploadType.THUMBNAIL);

            video.updateContentPath(videoUrl, thumbnailUrl);
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

    public Page<Video> findAll(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }

    public List<Video> findAll() {
        List<Video> findVideos = videoRepository.findAll();
        Collections.shuffle(findVideos);

        return findVideos.stream()
                .limit(12)
                .collect(toList());
    }

    public List<Video> findOrderVideos() { //구독자만
        List<Video> findVideos = videoRepository.findAll();
        Collections.shuffle(findVideos);

        return findVideos.stream()
                .limit(12)
                .collect(toList());
    }

    public List<Video> findPopularityVideos() {
        return videoRepository.findTop12ByOrderByViewsDesc();
    }
}