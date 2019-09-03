package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.VideoLike;
import com.wootube.ioi.domain.repository.VideoLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class VideoLikeServiceTest {
    private static final Long USER_ID = 1L;
    private static final Long VIDEO_ID = 1L;
    @InjectMocks
    private VideoLikeService videoLikeService;
    @Mock
    private UserService userService;
    @Mock
    private VideoService videoService;
    @Mock
    private VideoLikeRepository videoLikeRepository;

    @Test
    @DisplayName("해당 비디오에서 사용자가 좋아요를 눌렀는지 여부를 반환한다.")
    void existsVideoLike() {
        given(videoLikeRepository.existsByVideoIdAndLikeUserId(VIDEO_ID, USER_ID)).willReturn(true);

        assertTrue(videoLikeService.existsVideoLike(VIDEO_ID, USER_ID));
    }

    @Test
    @DisplayName("좋아요를 생성한다.")
    void save() {
        VideoLike videoLike = new VideoLike();
        given(videoLikeRepository.save(videoLike)).willReturn(videoLike);

        videoLikeService.likeVideo(VIDEO_ID, USER_ID);

        verify(videoLikeRepository).save(videoLike);
    }

    @Test
    @DisplayName("좋아요를 취소한다.")
    void cancel() {
        VideoLike videoLike = new VideoLike();
        given(videoLikeRepository.save(videoLike)).willReturn(videoLike);
        given(videoLikeService.existsVideoLike(VIDEO_ID, USER_ID)).willReturn(true);

        videoLikeService.likeVideo(VIDEO_ID, USER_ID);

        verify(videoLikeRepository).deleteByVideoIdAndLikeUserId(VIDEO_ID, USER_ID);
    }

    @Test
    @DisplayName("해당 비디오의 좋아요 개수를 반환한다.")
    void count() {
        given(videoLikeRepository.countByVideoId(VIDEO_ID)).willReturn(1l);

        assertThat(videoLikeService.getVideoLikeCount(VIDEO_ID).getCount()).isEqualTo(1l);
    }
}
