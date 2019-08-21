package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.CommentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CommentApiControllerTest extends CommonControllerTest {
    @Test
    @DisplayName("댓글을 정상적으로 생성한다.")
    void createComment() {
        signup();
        String sessionId = login();
        String videoId = getSaveVideoId(sessionId);
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
        when().
                post(basicPath() + "/api/videos/" + videoId + "/comments").
        then().
                statusCode(201).
                body("id", is(not(empty()))).
                body("contents", equalTo(SAVE_COMMENT_RESPONSE.getContents())).
                body("updateTime", is(not(empty())));
    }

    @Test
    @DisplayName("영상이 없는 경우 예외가 발생한다.")
    void createCommentFail() {
        signup();
        String sessionId = login();
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
        when().
                post(basicPath() + "/api/videos/" + NOT_EXIST_VIDEO_ID + "/comments").
        then().
                statusCode(400);
    }

    @Test
    @DisplayName("댓글을 정상적으로 수정한다.")
    void updateComment() {
        signup();
        String sessionId = login();
        String videoId = getSaveVideoId(sessionId);
        int commentId = getCommentId2(sessionId, videoId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/"+ videoId +"/comments/" + commentId).
        then().
                statusCode(204);
    }

    @Test
    @DisplayName("댓글을 정상적으로 수정한다.")
    void updateCommentFail2() {
        signup();
        String sessionId = login();
        String videoId = getSaveVideoId(sessionId);
        int commentId = getCommentId2(sessionId, videoId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/"+ videoId +"/comments/" + commentId).
                then().
                statusCode(204);
    }


    @Test
    @DisplayName("댓글 ID가 1번인 댓글을 삭제한다.")
    void deleteComment() {
        signup();
        String sessionId = login();
        String videoId = getSaveVideoId(sessionId);
        int commentId = getCommentId2(sessionId, videoId);

        given().
                cookie("JSESSIONID", sessionId).
        when().
                delete(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId).
        then().
                statusCode(204);
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
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제하는 경우 예외발생")
    void deleteCommentFail() {
        given().
                when().
                delete(basicPath() + "/api/videos/1/comments/" + NOT_EXIST_COMMENT_ID).
                then().
                statusCode(400);
    }
}
