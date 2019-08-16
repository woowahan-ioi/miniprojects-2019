package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.ReplyRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class ReplyControllerTest extends CommentCommonControllerTest {
    @Test
    @DisplayName("답글을 생성한다.")
    void createReply() {
        saveCommentAndSaveReply()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.contents").isEqualTo(SAVE_REPLY_RESPONSE.getContents())
                .jsonPath("$.updateTime").isNotEmpty()
        ;
    }

    @Test
    @DisplayName("답글을 수정한다.")
    void updateReply() {
        saveCommentAndSaveReply();

        webTestClient.put()
                .uri("/watch/1/comments/1/replies/" + EXIST_REPLY_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new ReplyRequestDto(SAVE_REPLY_RESPONSE.getContents())), ReplyRequestDto.class)
                .exchange()
                .expectStatus().isNoContent()
        ;
    }

    @Test
    @DisplayName("답글이 존재하지 않는 경우 예외가 발생한다.")
    void notExistReplyUpdate() {
        saveCommentAndSaveReply();

        webTestClient.put()
                .uri("/watch/1/comments/1/replies/" + NOT_EXIST_REPLY_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new ReplyRequestDto(SAVE_REPLY_RESPONSE.getContents())), ReplyRequestDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo(NOT_FOUND_REPLY_EXCEPTION_MESSAGE)
        ;
    }

    @Test
    @DisplayName("댓글이 존재하지 않는 경우, 답글을 수정할 때 예외가 발생한다.")
    void notExistCommentUpdate() {
        saveCommentAndSaveReply();

        webTestClient.put()
                .uri("/watch/1/comments/" + NOT_EXIST_COMMENT_ID + "/replies/" + EXIST_REPLY_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new ReplyRequestDto(SAVE_REPLY_RESPONSE.getContents())), ReplyRequestDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo(NOT_FOUND_COMMENT_EXCEPTION_MESSAGE)
        ;
    }

    private WebTestClient.ResponseSpec saveCommentAndSaveReply() {
        loginAndSaveComment();

        return webTestClient.post()
                .uri("/watch/1/comments/1/replies")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new ReplyRequestDto(SAVE_REPLY_RESPONSE.getContents())), ReplyRequestDto.class)
                .exchange();
    }
}
