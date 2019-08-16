package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.dto.CommentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class CommentControllerTest extends CommentCommonControllerTest {
    //로그인 안 했을 시 실패하는 테스트 추가
    //요청하는 Video id와 Comment의 video Id가 다른 경우 실패하는 테스트 추가
    //요청하는 Writer id와 Comment의 Writer Id가 다른 경우 실패하는 테스트 추가

    @Test
    @DisplayName("댓글을 생성한다.")
    void createComment() {
        loginAndSaveComment()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(SAVE_COMMENT_RESPONSE.getContents())
                .jsonPath("$.id").isEqualTo(SAVE_COMMENT_RESPONSE.getId())
                .jsonPath("$.updateTime").isNotEmpty()
        ;
    }

    @Test
    @DisplayName("댓글 ID가 1번인 댓글을 수정한다.")
    void updateComment() {
        loginAndSaveComment();

        webTestClient.put()
                .uri("/watch/1/comments/" + EXIST_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new CommentRequestDto(UPDATE_COMMENT_RESPONSE.getContents())), CommentRequestDto.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("존재하지 않는 댓글 수정하는 경우 예외발생")
    void updateCommentFail() {
        webTestClient.put()
                .uri("/watch/1/comments/" + NOT_EXIST_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new CommentRequestDto(UPDATE_COMMENT_RESPONSE.getContents())), CommentRequestDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo(NOT_FOUND_COMMENT_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("댓글 ID가 1번인 댓글을 삭제한다.")
    void deleteComment() {
        loginAndSaveComment();

        webTestClient.delete()
                .uri("/watch/1/comments/" + EXIST_COMMENT_ID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제하는 경우 예외발생")
    void deleteCommentFail() {
        webTestClient.delete()
                .uri("/watch/1/comments/" + NOT_EXIST_COMMENT_ID)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo(NOT_FOUND_COMMENT_EXCEPTION_MESSAGE);
    }

    private String login() {
        User user = new User("hyo", "hyo@naver.com", "password123!");

        return webTestClient.post()
                .uri("/login")
                .body(Mono.just(user), User.class)
                .exchange()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();
    }
}
