package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.CommentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CommentApiControllerTest extends CommentCommonControllerTest {
    //로그인 안 했을 시 실패하는 테스트 추가
    //요청하는 Video id와 Comment의 video Id가 다른 경우 실패하는 테스트 추가
    //요청하는 Writer id와 Comment의 Writer Id가 다른 경우 실패하는 테스트 추가

    @Test
    @DisplayName("댓글을 생성한다.")
    void createComment() {
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
        when().
                post(basicPath() + "/api/videos/1/comments").
        then().
                statusCode(201).
                body("id", is(not(empty()))).
                body("contents", equalTo(SAVE_COMMENT_RESPONSE.getContents())).
                body("updateTime", is(not(empty())));

//        webTestClient.post()
//                .uri("/api/videos/1/comments")
//                //.cookie("JSESSIONID", loginSessionId)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(new CommentRequestDto(SAVE_COMMENT_RESPONSE.getContents())), CommentRequestDto.class)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .jsonPath("$.contents").isEqualTo(SAVE_COMMENT_RESPONSE.getContents())
//                .jsonPath("$.id").isEqualTo(SAVE_COMMENT_RESPONSE.getId())
//                .jsonPath("$.updateTime").isNotEmpty()
//        ;
    }

    @Test
    @DisplayName("댓글 ID가 1번인 댓글을 수정한다.")
    void updateComment() {
        int commentId = getCommentId();

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/1/comments/" + commentId).
        then().
                statusCode(204);

//        webTestClient.put()
//                .uri("/api/videos/1/comments/" + commentId)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(new CommentRequestDto(UPDATE_COMMENT_RESPONSE.getContents())), CommentRequestDto.class)
//                .exchange()
//                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("존재하지 않는 댓글 수정하는 경우 예외발생")
    void updateCommentFail() {
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/1/comments/" + NOT_EXIST_COMMENT_ID).
        then().
                statusCode(400);

//        webTestClient.put()
//                .uri("/api/videos/1/comments/" + NOT_EXIST_COMMENT_ID)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(new CommentRequestDto(UPDATE_COMMENT_RESPONSE.getContents())), CommentRequestDto.class)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$").isEqualTo(NOT_FOUND_COMMENT_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("댓글 ID가 1번인 댓글을 삭제한다.")
    void deleteComment() {
        int commentId = getCommentId();

        given().
        when().
                delete(basicPath() + "/api/videos/1/comments/" + commentId).
        then().
                statusCode(204);

//        webTestClient.delete()
//                .uri("/api/videos/1/comments/" + commentId)
//                .exchange()
//                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제하는 경우 예외발생")
    void deleteCommentFail() {
        given().
        when().
                delete(basicPath() + "/api/videos/1/comments/" + NOT_EXIST_COMMENT_ID).
        then().
                statusCode(400);

//        webTestClient.delete()
//                .uri("/api/videos/1/comments/" + NOT_EXIST_COMMENT_ID)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$").isEqualTo(NOT_FOUND_COMMENT_EXCEPTION_MESSAGE);
    }

//    private String login() {
//        User user = new User("hyo", "hyo@naver.com", "password123!");
//
//        return webTestClient.post()
//                .uri("/login")
//                .body(Mono.just(user), User.class)
//                .exchange()
//                .returnResult(String.class)
//                .getResponseCookies()
//                .getFirst("JSESSIONID")
//                .getValue();
//    }
}
