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
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionValue).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments").
                then().
                statusCode(201).
                body("id", is(not(empty()))).
                body("contents", equalTo(SAVE_COMMENT_RESPONSE.getContents())).
                body("updateTime", is(not(empty())));
    }

    @Test
    @DisplayName("다른 유저의 영상에서 댓글을 정상적으로 생성한다.")
    void createComment2() {
        String secondUserSession = login(USER_B_LOGIN_REQUEST_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", secondUserSession).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments").
                then().
                statusCode(201).
                body("id", is(not(empty()))).
                body("contents", equalTo(SAVE_COMMENT_RESPONSE.getContents())).
                body("updateTime", is(not(empty())));
    }

    @Test
    @DisplayName("영상이 없는 경우 댓글을 생성하면 예외가 발생한다.")
    void createCommentFail() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionValue).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/" + NOT_EXIST_VIDEO_ID + "/comments").
                then().
                statusCode(302);
    }

    @Test
    @DisplayName("댓글을 정상적으로 수정한다.")
    void updateComment() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionValue).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT).
                then().
                statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 수정하는 경우 예외발생")
    void updateCommentFail() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionValue).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + NOT_EXIST_COMMENT_ID).
                then().
                statusCode(400);
    }

    @Test
    @DisplayName("첫번째 유저의 댓글을 두번째 유저가 수정하는 경우 예외가 발생한다.")
    void updateCommentFail2() {
        String secondUserSession = login(USER_B_LOGIN_REQUEST_DTO);

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", secondUserSession).
                body(CommentRequestDto.of(UPDATE_COMMENT_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT).
                then().
                statusCode(400);
    }

    @Test
    @DisplayName("댓글을 정상적으로 삭제한다.")
    void deleteComment() {
        String sessionId = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                cookie("JSESSIONID", sessionId).
                when().
                delete(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT).
                then().
                statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제하는 경우 예외가 발생한다.")
    void deleteCommentFail() {
        String sessionId = login(USER_A_LOGIN_REQUEST_DTO);

        given().
                cookie("JSESSIONID", sessionId).
                when().
                delete(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + NOT_EXIST_COMMENT_ID).
                then().
                statusCode(400);
    }

    @Test
    @DisplayName("다른 유저의 댓글을 삭제하는 경우 예외가 발생한다.")
    void deleteCommentFail2() {
        String secondUserSession = login(USER_B_LOGIN_REQUEST_DTO);

        given().
                cookie("JSESSIONID", secondUserSession).
                when().
                delete(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT).
                then().
                statusCode(400);
    }

    @Test
    void sortCommentByUpdateTime() {
        given().
                when().
                get(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/sort/updatetime").
                then().
                statusCode(200).
                body("", is(not(empty())));
    }
}
