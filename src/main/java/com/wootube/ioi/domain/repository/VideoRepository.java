package com.wootube.ioi.domain.repository;

import java.util.List;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT v FROM Video v ORDER BY RAND()")
    Page<Video> findAllRandom(Pageable pageable);

    List<Video> findByWriter(User writer);
}
