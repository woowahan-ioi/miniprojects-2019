package com.wootube.ioi.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

public class VideoApiControllerTest extends CommonControllerTest {
    @Test
    @DisplayName("사용자가 좋아요를 처음 눌렀을 때 좋아요 개수가 증가한다.")
    void createVideoLike() {
        loginAndRequest(HttpMethod.POST, "/api/videos/" + USER_B_VIDEO_ID + "/likes", USER_A_LOGIN_REQUEST_DTO)
                .expectStatus().isOk()
                .expectBody().json("{count : 1}");
    }

    @Test
    @DisplayName("사용자가 좋아요를 중복으로 눌렀을 때 좋아요 개수가 감소한다.")
    void duplicateVideoLike() {
        loginAndRequest(HttpMethod.POST, "/api/videos/" + USER_B_VIDEO_ID + "/likes", USER_A_LOGIN_REQUEST_DTO)
                .expectStatus().isOk()
                .expectBody().json("{count : 0}");
    }

    @Test
    @DisplayName("권한이 없는 사용자가 좋아요를 눌렀을 때 좋아요 개수가 증가하지 않는다.")
    void unAuthorizedUserVideoLike() {
        request(HttpMethod.POST, "/api/videos/" + USER_B_VIDEO_ID + "/likes")
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("좋아요 개수를 반환한다.")
    void getVideoLikeCount() {
        request(HttpMethod.GET, "/api/videos/" + USER_C_VIDEO_ID + "/likes/counts")
                .expectStatus().isOk()
                .expectBody().json("{count : 2}");
    }
}
