package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
