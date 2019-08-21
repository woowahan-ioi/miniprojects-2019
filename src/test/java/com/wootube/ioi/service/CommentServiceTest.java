package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.exception.NotFoundCommentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CommentServiceTest {
    private static final Long EXIST_COMMENT_ID = 1L;
    private static final Long EXIST_VIDEO_ID = 1L;
    private static final Long NOT_EXIST_COMMENT_ID = 0L;

    private static final String COMMENT1_CONTENTS = "Comment Contents 1";
    private static final String COMMENT2_CONTENTS = "Comment Contents 2";

    private static final CommentRequestDto COMMENT_REQUEST1 = CommentRequestDto.of(COMMENT1_CONTENTS);
    private static final CommentRequestDto COMMENT_REQUEST2 = CommentRequestDto.of(COMMENT2_CONTENTS);

    private static final Video VIDEO = new Video("test", "test");
    private static final User WRITER = new User("name", "test@email.com", "1234qwer");

    private static final Comment COMMENT1 = Comment.of(COMMENT_REQUEST1.getContents(), VIDEO, WRITER);

    private static final CommentResponseDto COMMENT_RESPONSE1 = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents 1",
            LocalDateTime.now());
    private static final CommentResponseDto COMMENT_RESPONSE2 = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents 2",
            LocalDateTime.now());

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private VideoService videoService;

    @Mock
    private UserService userService;

    @Mock
    private Comment updateComment;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글을 생성한다.")
    void save() {
        given(commentRepository.save(COMMENT1)).willReturn(COMMENT1);
        given(modelMapper.map(COMMENT1, CommentResponseDto.class)).willReturn(COMMENT_RESPONSE1);
        given(videoService.findVideo(EXIST_COMMENT_ID)).willReturn(VIDEO);
        given(userService.findByEmail(WRITER.getEmail())).willReturn(WRITER);

        commentService.save(COMMENT_REQUEST1, VIDEO.getId(), WRITER.getEmail());

        verify(commentRepository).save(COMMENT1);
    }

    @Test
    @DisplayName("댓글을 수정한다.")
    void update() {
        given(commentRepository.findById(EXIST_COMMENT_ID)).willReturn(Optional.of(updateComment));
        given(modelMapper.map(updateComment, CommentResponseDto.class)).willReturn(COMMENT_RESPONSE2);
        given(videoService.findVideo(EXIST_COMMENT_ID)).willReturn(VIDEO);
        given(userService.findByEmail(WRITER.getEmail())).willReturn(WRITER);

        commentService.update(EXIST_COMMENT_ID, WRITER.getEmail(), EXIST_COMMENT_ID, COMMENT_REQUEST2);

        verify(updateComment).update(WRITER, VIDEO, COMMENT_REQUEST2.getContents());
    }

    @Test
    @DisplayName("없는 댓글을 수정하는 경우 예외를 던진다.")
    void updateFail() {
        assertThrows(NotFoundCommentException.class, () -> commentService.update(NOT_EXIST_COMMENT_ID, WRITER.getEmail(), EXIST_COMMENT_ID, COMMENT_REQUEST1));
    }

    @Test
    @DisplayName("댓글을 삭제한다.")
    void delete() {
        given(commentRepository.findById(EXIST_COMMENT_ID)).willReturn(Optional.of(COMMENT1));
        given(userService.findByEmail(WRITER.getEmail())).willReturn(WRITER);
        given(videoService.findVideo(EXIST_VIDEO_ID)).willReturn(VIDEO);

        commentService.delete(EXIST_COMMENT_ID, WRITER.getEmail(), EXIST_VIDEO_ID);

        verify(commentRepository).deleteById(EXIST_COMMENT_ID);
    }

    @Test
    @DisplayName("없는 댓글을 삭제하는 경우 예외를 던진다.")
    void deleteFail() {
        assertThrows(NotFoundCommentException.class, () -> commentService.delete(NOT_EXIST_COMMENT_ID, WRITER.getEmail(), EXIST_VIDEO_ID));
    }
}
