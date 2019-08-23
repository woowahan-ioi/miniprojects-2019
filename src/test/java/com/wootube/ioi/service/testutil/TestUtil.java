package com.wootube.ioi.service.testutil;

import java.time.LocalDateTime;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.Reply;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;

public class TestUtil {
    public static final Video VIDEO = new Video("test", "test");
    public static final User WRITER = new User("name", "test@email.com", "1234qwer");

    public static final Long EXIST_COMMENT_ID = 1L;
    public static final Long EXIST_VIDEO_ID = 1L;
    public static final Long NOT_EXIST_COMMENT_ID = 0L;

    public static final String COMMENT1_CONTENTS = "Comment Contents 1";
    public static final String COMMENT2_CONTENTS = "Comment Contents 2";

    public static final CommentRequestDto COMMENT_REQUEST1 = CommentRequestDto.of(COMMENT1_CONTENTS);
    public static final CommentRequestDto COMMENT_REQUEST2 = CommentRequestDto.of(COMMENT2_CONTENTS);

    public static final Comment COMMENT1 = Comment.of(COMMENT_REQUEST1.getContents(), VIDEO, WRITER);

    public static final CommentResponseDto COMMENT_RESPONSE1 = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents 1",
            LocalDateTime.now(), WRITER);
    public static final CommentResponseDto COMMENT_RESPONSE2 = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents 2",
            LocalDateTime.now(), WRITER);

    public static final String REPLY1_CONTENTS = "Reply Contents 1";
    public static final String REPLY2_CONTENTS = "Reply Contents 2";

    public static final ReplyRequestDto REPLY_REQUEST_DTO1 = ReplyRequestDto.of(REPLY1_CONTENTS);
    public static final ReplyRequestDto REPLY_REQUEST_DTO2 = ReplyRequestDto.of(REPLY2_CONTENTS);

    public static final Reply REPLY1 = Reply.of(REPLY1_CONTENTS, COMMENT1, WRITER);
    public static final Reply REPLY2 = Reply.of(REPLY2_CONTENTS, COMMENT1, WRITER);

    public static final ReplyResponseDto REPLY_RESPONSE_DTO1 = ReplyResponseDto.of(REPLY1.getId(), REPLY1.getContents(), REPLY1.getUpdateTime(), WRITER);
    public static final ReplyResponseDto REPLY_RESPONSE_DTO2 = ReplyResponseDto.of(REPLY2.getId(), REPLY2.getContents(), REPLY2.getUpdateTime(), WRITER);

}
