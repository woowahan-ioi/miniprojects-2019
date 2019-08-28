package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sun.jvm.hotspot.debugger.Page;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByWriter(User writer);
    List<Video> findTop12ByOrderByViewsDesc();
}
