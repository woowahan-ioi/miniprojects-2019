package com.wootube.ioi.service;

import javax.transaction.Transactional;

import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.service.exception.NotFoundVideoException;
import com.wootube.ioi.service.util.FileUploader;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {
    private final FileUploader fileUploader;
    private final ModelMapper modelMapper;

    private final VideoRepository videoRepository;

    public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository) {
        this.fileUploader = fileUploader;
        this.modelMapper = modelMapper;
        this.videoRepository = videoRepository;
    }

    public VideoResponseDto create(MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
        String videoUrl = fileUploader.uploadFile(uploadFile);
        String originFileName = uploadFile.getOriginalFilename();
        videoRequestDto.setContentPath(videoUrl);

        Video video = modelMapper.map(videoRequestDto, Video.class);
        video.setOriginFileName(originFileName);

        return modelMapper.map(videoRepository.save(video), VideoResponseDto.class);
    }

    public VideoResponseDto findById(Long id) {
        return modelMapper.map(findVideo(id), VideoResponseDto.class);
    }

    private Video findVideo(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(NotFoundVideoException::new);
    }

    @Transactional
    public void update(Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
        Video video = findVideo(id);
        if (!uploadFile.isEmpty()) {
            fileUploader.deleteFile(video.getOriginFileName());
            String videoUrl = fileUploader.uploadFile(uploadFile);
            video.setContentPath(videoUrl);
        }
        video.update(modelMapper.map(videoRequestDto, Video.class));
        videoRepository.save(video);
    }

    @Transactional
    public void deleteById(Long id) {
        Video video = findVideo(id);
        fileUploader.deleteFile(video.getOriginFileName());
        videoRepository.deleteById(id);
    }
}
