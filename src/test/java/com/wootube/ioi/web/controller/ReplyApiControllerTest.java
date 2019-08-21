package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.ReplyRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReplyApiControllerTest extends CommonControllerTest {
    @Test
    @DisplayName("답글을 생성한다.")
    void createReply() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
        when().
                post(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId + "/replies").
        then().
                statusCode(201).
                body("id", is(not(empty()))).
                body("contents", equalTo(SAVE_REPLY_RESPONSE.getContents())).
                body("updateTime", is(not(empty())));
    }

    @Test
    @DisplayName("답글을 수정한다.")
    void updateReply() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);
        int replyId = getSavedReplyId(videoId, commentId, sessionId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(ReplyRequestDto.of(UPDATE_REPLY_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + replyId).
        then().
                statusCode(204);
    }

    @Test
    @DisplayName("답글이 존재하지 않는 경우 예외가 발생한다.")
    void notExistReplyUpdate() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + NOT_EXIST_REPLY_ID).
        then().
                statusCode(400);
    }

    @Test
    @DisplayName("댓글이 존재하지 않는 경우, 답글을 수정할 때 예외가 발생한다.")
    void notExistCommentUpdate() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);
        int replyId = getSavedReplyId(videoId, commentId, sessionId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/" + videoId + "/comments/" + NOT_EXIST_COMMENT_ID + "/replies/" + replyId).
        then().
                statusCode(400);
    }

    @Test
    @DisplayName("답글을 삭제한다.")
    void deleteReply() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);
        int replyId = getSavedReplyId(videoId, commentId, sessionId);

        given().
                cookie("JSESSIONID", sessionId).
        when().
                delete(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + replyId).
        then().
                statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 답글을 삭제하는 경우 예외가 발생한다.")
    void deleteReplyFail2() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);

        given().
                cookie("JSESSIONID", sessionId).
        when().
                delete(basicPath() + "/api/videos/"+ videoId +"/comments/" + commentId + "/replies/" + NOT_EXIST_REPLY_ID).
        then().
                statusCode(400);
    }
}
