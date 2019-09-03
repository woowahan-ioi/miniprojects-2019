package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class ReplyApiControllerTest extends CommonControllerTest {
    @Test
    @DisplayName("답글을 생성한다.")
    void createReply() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionValue).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT + "/replies").
                then().
                statusCode(201).
                body("id", is(not(empty()))).
                body("contents", equalTo(SAVE_REPLY_RESPONSE.getContents())).
                body("updateTime", is(not(empty())));
    }

    @Test
    @DisplayName("답글을 수정한다.")
    void updateReply() {
        String sessionId = login(USER_A_LOGIN_REQUEST_DTO);
        int replyId = getSavedReplyId(USER_A_VIDEO_ID, USER_A_VIDEO_USER_A_COMMENT, sessionId);
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(ReplyRequestDto.of(UPDATE_REPLY_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT + "/replies/" + replyId).
                then().
                statusCode(204);
    }

    @Test
    @DisplayName("답글이 존재하지 않는 경우 예외가 발생한다.")
    void notExistReplyUpdate() {
        String sessionId = login(USER_A_LOGIN_REQUEST_DTO);
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT + "/replies/" + NOT_EXIST_REPLY_ID).
                then().
                statusCode(400);
    }

    @Test
    @DisplayName("댓글이 존재하지 않는 경우, 답글을 수정할 때 예외가 발생한다.")
    void notExistCommentUpdate() {
        String sessionId = login(USER_A_LOGIN_REQUEST_DTO);
        int replyId = getSavedReplyId(USER_A_VIDEO_ID, USER_A_VIDEO_USER_A_COMMENT, sessionId);
        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                put(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + NOT_EXIST_COMMENT_ID + "/replies/" + replyId).
                then().
                statusCode(400);
    }

    @Test
    @DisplayName("답글을 삭제한다.")
    void deleteReply() {
        String sessionId = login(USER_A_LOGIN_REQUEST_DTO);
        int replyId = getSavedReplyId(USER_A_VIDEO_ID, USER_A_VIDEO_USER_A_COMMENT, sessionId);
        given().
                cookie("JSESSIONID", sessionId).
                when().
                delete(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT + "/replies/" + replyId).
                then().
                statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 답글을 삭제하는 경우 예외가 발생한다.")
    void deleteReplyFail2() {
        String sessionId = login(USER_A_LOGIN_REQUEST_DTO);
        given().
                cookie("JSESSIONID", sessionId).
                when().
                delete(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT + "/replies/" + NOT_EXIST_REPLY_ID).
                then().
                statusCode(400);
    }

    @Test
    @DisplayName("답글을 최신순으로 정렬한다.")
    void sortReplyByUpdateTime() {
        String sessionValue = login(USER_A_LOGIN_REQUEST_DTO);
        getSavedReplyId(USER_A_VIDEO_ID, USER_A_VIDEO_USER_A_COMMENT, sessionValue);
        getSavedReplyId(USER_A_VIDEO_ID, USER_A_VIDEO_USER_A_COMMENT, sessionValue);

        List<ReplyResponseDto> replies =
                given().
                        when().
                        get(basicPath() + "/api/videos/" + USER_A_VIDEO_ID + "/comments/" + USER_A_VIDEO_USER_A_COMMENT + "/replies/sort/updatetime").
                        then().
                        statusCode(200).
                        extract().
                        response().
                        jsonPath().
                        getList(".", ReplyResponseDto.class);

        assertThat(replies.size()).isEqualTo(2);
    }

    int getSavedReplyId(Long videoId, Long commentId, String sessionId) {
        return given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie("JSESSIONID", sessionId).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId + "/replies").
                getBody().
                jsonPath().
                get("id");
    }
}
