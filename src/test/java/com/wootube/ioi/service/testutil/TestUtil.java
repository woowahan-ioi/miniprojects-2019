package com.wootube.ioi.service.testutil;

import com.wootube.ioi.domain.model.*;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;

import java.time.LocalDateTime;

public class TestUtil {
    protected static final String TITLE = "title";
    protected static final String DESCRIPTION = "description";
    protected static final String CONTENTS = "<<testVideo data>>";

    protected static final String DIRECTORY = "wootube";
    protected static final String VIDEO_FILE_NAME = "testVideo.mp4";
    protected static final String THUMBNAIL_FILE_NAME = "testVideoThumbnail.png";

    protected static final String PROFILE_IMAGE_FILE_NAME = "testImage.png";
    protected static final String PROFILE_IMAGE_URL = String.format("%s/%s", DIRECTORY, PROFILE_IMAGE_FILE_NAME);
    protected static final String UPDATE_PROFILE_IMAGE_FILE_NAME = "changeTestImage.png";

    protected static final ProfileImage PROFILE_IMAGE = new ProfileImage(PROFILE_IMAGE_URL, PROFILE_IMAGE_FILE_NAME);
    protected static final ProfileImage UPDATE_PROFILE_IMAGE = new ProfileImage(PROFILE_IMAGE_URL, UPDATE_PROFILE_IMAGE_FILE_NAME);

    protected static final Long USER_ID = 1L;

    protected static final Long ID = 1L;

    protected static final Video VIDEO = new Video("test", "test");
    protected static final User WRITER = new User("name", "test@email.com", "1234qwer");

    protected static final Long EXIST_COMMENT_ID = 1L;
    protected static final Long EXIST_VIDEO_ID = 1L;
    protected static final Long NOT_EXIST_COMMENT_ID = 0L;
    protected static final CommentResponseDto COMMENT_RESPONSE1 = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents 1",
            LocalDateTime.now(), WRITER.getName(), "profile");
    protected static final CommentResponseDto COMMENT_RESPONSE2 = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents 2",
            LocalDateTime.now(), WRITER.getName(), "profile");

    private static final String COMMENT1_CONTENTS = "Comment Contents 1";
    protected static final CommentRequestDto COMMENT_REQUEST1 = CommentRequestDto.of(COMMENT1_CONTENTS);

    private static final String COMMENT2_CONTENTS = "Comment Contents 2";
    protected static final CommentRequestDto COMMENT_REQUEST2 = CommentRequestDto.of(COMMENT2_CONTENTS);

    private static final String REPLY1_CONTENTS = "Reply Contents 1";
    protected static final Comment COMMENT1 = Comment.of(COMMENT_REQUEST1.getContents(), VIDEO, WRITER);
    protected static final Reply REPLY1 = Reply.of(REPLY1_CONTENTS, COMMENT1, WRITER);

    protected static final ReplyResponseDto REPLY_RESPONSE_DTO1 = ReplyResponseDto.of(REPLY1.getId(), REPLY1.getContents(), REPLY1.getUpdateTime(), WRITER.getName(), "profile");
    protected static final ReplyRequestDto REPLY_REQUEST_DTO1 = ReplyRequestDto.of(REPLY1_CONTENTS);

}