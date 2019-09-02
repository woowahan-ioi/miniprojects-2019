package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.VideoLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {
    long countByVideoId(Long videoId);

    void deleteByVideoIdAndLikeUserId(Long videoId, Long userId);

    boolean existsByVideoIdAndLikeUserId(Long videoId, Long userId);
}
