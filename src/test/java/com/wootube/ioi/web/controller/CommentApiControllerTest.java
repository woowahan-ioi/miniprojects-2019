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
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);

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
    @DisplayName("다른 유저의 영상에서 댓글을 정상적으로 생성한다.")
    void createComment2() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String firstUser = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(firstUser);

        signup(SIGN_UP_SECOND_USER_DTO);
        String secondUser = login(LOG_IN_SECOND_USER_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", secondUser).
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
    @DisplayName("영상이 없는 경우 댓글을 생성하면 예외가 발생한다.")
    void createCommentFail() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);

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
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);

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
    @DisplayName("존재하지 않는 댓글 수정하는 경우 예외발생")
    void updateCommentFail() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/"+ videoId +"/comments/" + NOT_EXIST_COMMENT_ID).
        then().
                statusCode(400);
    }

    @Test
    @DisplayName("첫번째 유저의 댓글을 두번째 유저가 수정하는 경우 예외가 발생한다.")
    void updateCommentFail2() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String firstUser = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoIdByFirstUser = getSavedVideoId(firstUser);
        int commentIdByFirstUser = getSavedCommentId(firstUser, videoIdByFirstUser);

        signup(SIGN_UP_SECOND_USER_DTO);
        String secondUser = login(LOG_IN_SECOND_USER_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", secondUser).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/"+ videoIdByFirstUser +"/comments/" + commentIdByFirstUser).
        then().
                statusCode(400);
    }

    @Test
    @DisplayName("다른 비디오 댓글을 수정하는 경우 예외가 발생한다.")
    void updateCommentFail3() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String firstUser = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(firstUser);
        int commentIdByFirstUser = getSavedCommentId(firstUser, videoId);

        signup(SIGN_UP_SECOND_USER_DTO);
        String secondUser = login(LOG_IN_SECOND_USER_DTO);
        int commentIdBySecondUser = getSavedCommentId(firstUser, videoId);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", secondUser).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
        when().
                put(basicPath() + "/api/videos/"+ videoId +"/comments/" + commentIdByFirstUser).
        then().
                statusCode(400);
    }

    @Test
    @DisplayName("댓글을 정상적으로 삭제한다.")
    void deleteComment() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);
        int commentId = getSavedCommentId(sessionId, videoId);

        given().
                cookie("JSESSIONID", sessionId).
        when().
                delete(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId).
        then().
                statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제하는 경우 예외가 발생한다.")
    void deleteCommentFail() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String sessionId = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(sessionId);

        given().
                cookie("JSESSIONID", sessionId).
        when().
                delete(basicPath() + "/api/videos/" + videoId + "/comments/" + NOT_EXIST_COMMENT_ID).
        then().
                statusCode(400);
    }

    @Test
    @DisplayName("다른 유저의 댓글을 삭제하는 경우 예외가 발생한다.")
    void deleteCommentFail2() {
        signup(SIGN_UP_COMMON_REQUEST_DTO);
        String firstUser = login(LOG_IN_COMMON_REQUEST_DTO);
        String videoId = getSavedVideoId(firstUser);
        int commentIdByFirstUser = getSavedCommentId(firstUser, videoId);

        signup(SIGN_UP_SECOND_USER_DTO);
        String secondUser = login(LOG_IN_SECOND_USER_DTO);

        given().
                cookie("JSESSIONID", secondUser).
        when().
                delete(basicPath() + "/api/videos/" + videoId + "/comments/" + commentIdByFirstUser).
        then().
                statusCode(400);
    }
}
