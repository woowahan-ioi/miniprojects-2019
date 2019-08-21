package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.service.exception.NotFoundUserException;
import com.wootube.ioi.service.exception.NotFoundVideoException;
import com.wootube.ioi.service.util.FileUploader;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

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

    public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto, Long userId) {
        String videoUrl = fileUploader.uploadFile(uploadFile);
        String originFileName = uploadFile.getOriginalFilename();

        Video video = modelMapper.map(videoRequestDto, Video.class);
        User writer = findUser(userId);

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
                .orElseThrow(NotFoundVideoException::new);
    }

    @Transactional
    public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
        Video video = findVideo(id);
        if (!uploadFile.isEmpty()) {
            fileUploader.deleteFile(video.getOriginFileName());
            String videoUrl = fileUploader.uploadFile(uploadFile);
            video.updateContentPath(videoUrl);
        }
        video.update(modelMapper.map(videoRequestDto, Video.class));
        videoRepository.save(video);
    }

    @Transactional
    public void deleteById(Long videoId) {
        Video video = findVideo(videoId);
//        Video writerVideo = video.matchWriter(userId);

        fileUploader.deleteFile(video.getOriginFileName());
        videoRepository.deleteById(video.getId());
    }

    public List<Video> findVideosByWriter(Long writerId) {
        User writer = findUser(writerId);
        return videoRepository.findByWriter(writer);
    }
}
