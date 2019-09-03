package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT v FROM Video v ORDER BY RAND()")
    Page<Video> findAllRandom(Pageable pageable);

    List<Video> findByWriter(User writer);
}
