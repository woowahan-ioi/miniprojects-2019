package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.model.VideoLike;
import com.wootube.ioi.domain.repository.VideoLikeRepository;
import com.wootube.ioi.service.dto.VideoLikeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoLikeService {

    private final UserService userService;
    private final VideoService videoService;
    private final VideoLikeRepository videoLikeRepository;

    @Autowired
    public VideoLikeService(UserService userService, VideoService videoService, VideoLikeRepository videoLikeRepository) {
        this.userService = userService;
        this.videoService = videoService;
        this.videoLikeRepository = videoLikeRepository;
    }

    public boolean existsVideoLike(Long videoId, Long userId) {
        return (videoLikeRepository.existsByVideoIdAndLikeUserId(videoId, userId));
    }

    @Transactional
    public VideoLikeResponseDto likeVideo(Long videoId, Long userId) {
        User user = userService.findByIdAndIsActiveTrue(userId);
        Video video = videoService.findById(videoId);
        VideoLike videoLike = new VideoLike(user, video);

        if (existsVideoLike(videoId, userId)) {
            delete(videoId, userId);
            return getVideoLikeCount(videoId);
        }

        videoLikeRepository.save(videoLike);
        return getVideoLikeCount(videoId);
    }

    private void delete(Long videoId, Long userId) {
        videoLikeRepository.deleteByVideoIdAndLikeUserId(videoId, userId);
    }

    public VideoLikeResponseDto getVideoLikeCount(Long videoId) {
        return new VideoLikeResponseDto(videoLikeRepository.countByVideoId(videoId));
    }
}