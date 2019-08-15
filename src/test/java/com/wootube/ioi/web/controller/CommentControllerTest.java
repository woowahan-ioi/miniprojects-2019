package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    private static final Long EXIST_COMMENT_ID = 1L;
    private static final Long NOT_EXIST_COMMENT_ID = 0L;

    private static final CommentResponseDto SAVE_COMMENT_RESPONSE = new CommentResponseDto(EXIST_COMMENT_ID,
            "Comment Contents",
            LocalDateTime.now());
    private static final CommentResponseDto UPDATE_COMMENT_RESPONSE = new CommentResponseDto(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now());

    public static final String NOT_FOUND_COMMENT_EXCEPTION_MESSAGE = "존재하지 않는 댓글입니다.";

    @Autowired
    private WebTestClient webTestClient;

    //로그인 안 했을 시 실패하는 테스트 추가
    //요청하는 Video id와 Comment의 video Id가 다른 경우 실패하는 테스트 추가
    //요청하는 Writer id와 Comment의 Writer Id가 다른 경우 실패하는 테스트 추가

    @Test
    public void createComment() {
        loginAndSaveComment()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(SAVE_COMMENT_RESPONSE.getContents())
                .jsonPath("$.id").isEqualTo(SAVE_COMMENT_RESPONSE.getId())
                .jsonPath("$.updateTime").isNotEmpty()
        ;
    }

    @Test
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

    private WebTestClient.ResponseSpec loginAndSaveComment() {
//        로그인한다.
//        String loginSessionId = login();

        return webTestClient.post()
                .uri("/watch/1/comments")
                //.cookie("JSESSIONID", loginSessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new CommentRequestDto(SAVE_COMMENT_RESPONSE.getContents())), CommentRequestDto.class)
                .exchange();
    }
}
