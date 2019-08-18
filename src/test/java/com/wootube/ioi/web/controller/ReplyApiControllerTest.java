package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.ReplyRequestDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ReplyApiControllerTest extends CommentCommonControllerTest {
    @Test
    @DisplayName("답글을 생성한다.")
    void createReply() {
        int commentId = getCommentId();

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/1/comments/" + commentId + "/replies").
                then().
                statusCode(201).
                body("id", is(not(empty()))).
                body("contents", equalTo(SAVE_REPLY_RESPONSE.getContents())).
                body("updateTime", is(not(empty())));
    }

    @Test
    @DisplayName("답글을 수정한다.")
    void updateReply() {
        int commentId = getCommentId();
        int replyId = getReplyId(commentId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(ReplyRequestDto.of("Update Contents")).
                when().
                put(basicPath() + "/api/videos/1/comments/" + commentId + "/replies/" + replyId).
                then().
                statusCode(204);

//        webTestClient.put()
//                .uri("/api/videos/1/comments/" + commentId + "/replies/" + replyId)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(new ReplyRequestDto(SAVE_REPLY_RESPONSE.getContents())), ReplyRequestDto.class)
//                .exchange()
//                .expectStatus().isNoContent()
//        ;
    }

    @Test
    @DisplayName("답글이 존재하지 않는 경우 예외가 발생한다.")
    void notExistReplyUpdate() {
        //에러 메시지 확인하기
        int commentId = getCommentId();

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/1/comments/" + commentId + "/replies/" + NOT_EXIST_REPLY_ID).
                then().
                statusCode(400);

//        webTestClient.put()
//                .uri("/api/videos/1/comments/" + commentId + "/replies/" + NOT_EXIST_REPLY_ID)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(new ReplyRequestDto(SAVE_REPLY_RESPONSE.getContents())), ReplyRequestDto.class)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$").isEqualTo(NOT_FOUND_REPLY_EXCEPTION_MESSAGE)
//        ;
    }

    @Test
    @DisplayName("댓글이 존재하지 않는 경우, 답글을 수정할 때 예외가 발생한다.")
    void notExistCommentUpdate() {
        int replyId = getReplyId();

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/1/comments/" + NOT_EXIST_COMMENT_ID + "/replies/" + replyId).
                then().
                statusCode(400);

//        webTestClient.put()
//                .uri("/api/videos/1/comments/" + NOT_EXIST_COMMENT_ID + "/replies/" + replyId)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(new ReplyRequestDto(SAVE_REPLY_RESPONSE.getContents())), ReplyRequestDto.class)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$").isEqualTo(NOT_FOUND_COMMENT_EXCEPTION_MESSAGE)
//        ;
    }

    @Test
    @DisplayName("답글을 삭제한다.")
    void deleteReply() {
        int commentId = getCommentId();
        int replyId = getReplyId(commentId);

        given().
                when().
                delete(basicPath() + "/api/videos/1/comments/" + commentId + "/replies/" + replyId).
                then().
                statusCode(204);

//        webTestClient.delete()
//                .uri("/api/videos/1/comments/" + commentId + "/replies/" + replyId)
//                .exchange()
//                .expectStatus().isNoContent()
//        ;
    }

    @Test
    @DisplayName("댓글에 해당하지 않은 답글을 삭제하는 경우 예외가 발생한다.")
    void deleteReplyFail() {
        int differentCommentId = getCommentId();
        int replyId = getReplyId();

        given().
                when().
                delete(basicPath() + "/api/videos/1/comments/" + differentCommentId + "/replies/" + replyId).
                then().
                statusCode(400);

//        webTestClient.delete()
//                .uri("/api/videos/1/comments/" + differentCommentId + "/replies/" + replyId)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$").isEqualTo("댓글에 일치하지 않은 답글 입니다.")
//        ;
    }

    @Test
    @DisplayName("존재하지 않는 답글을 삭제하는 경우 예외가 발생한다.")
    void deleteReplyFail2() {
        int commentId = getCommentId();

        given().
                when().
                delete(basicPath() + "/api/videos/1/comments/" + commentId + "/replies/" + NOT_EXIST_REPLY_ID).
                then().
                statusCode(400);

//        webTestClient.delete()
//                .uri("/api/videos/1/comments/" + commentId + "/replies/" + NOT_EXIST_REPLY_ID)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$").isEqualTo("존재하지 않는 답글 입니다.")
//        ;
    }
}
