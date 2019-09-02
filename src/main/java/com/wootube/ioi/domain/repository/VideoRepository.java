package com.wootube.ioi.domain.repository;

import java.util.List;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
	List<Video> findByWriter(User writer);

	List<Video> findTop12ByOrderByViewsDesc();

	List<Video> findTop20ByOrderByViewsDesc();

	List<Video> findTop12ByOrderByCreateTimeDesc();
}
