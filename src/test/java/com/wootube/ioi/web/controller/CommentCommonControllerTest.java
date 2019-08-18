package com.wootube.ioi.web.controller;

import java.time.LocalDateTime;

import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentCommonControllerTest {
    static final Long EXIST_COMMENT_ID = 1L;
    static final Long EXIST_REPLY_ID = 1L;
    static final Long NOT_EXIST_COMMENT_ID = 0L;
    static final Long NOT_EXIST_REPLY_ID = 0L;

    static final CommentResponseDto SAVE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents",
            LocalDateTime.now());
    static final CommentResponseDto UPDATE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now());
    static final ReplyResponseDto SAVE_REPLY_RESPONSE = ReplyResponseDto.of(EXIST_COMMENT_ID,
            "Reply Contents",
            LocalDateTime.now());

    static final String NOT_FOUND_COMMENT_EXCEPTION_MESSAGE = "존재하지 않는 댓글 입니다.";
    static final String NOT_FOUND_REPLY_EXCEPTION_MESSAGE = "존재하지 않는 답글 입니다.";

    @LocalServerPort
    private int port;

    String basicPath() {
        return "http://localhost:" + port;
    }

    int getCommentId() {
        return given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/1/comments").
                getBody().
                jsonPath().
                get("id");
    }

    int getReplyId() {
        int commentId = getCommentId();

        return given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/1/comments/" + commentId + "/replies").
                getBody().
                jsonPath().
                get("id");
    }

    int getReplyId(int commentId) {
        return given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                post(basicPath() + "/api/videos/1/comments/" + commentId + "/replies").
                getBody().
                jsonPath().
                get("id");
    }
}
