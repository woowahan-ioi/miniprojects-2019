package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.exception.NotFoundCommentException;
import com.wootube.ioi.service.testutil.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CommentServiceTest extends TestUtil {
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
        given(videoService.findById(EXIST_COMMENT_ID)).willReturn(VIDEO);
        given(userService.findByEmail(WRITER.getEmail())).willReturn(WRITER);

        commentService.save(COMMENT_REQUEST1, VIDEO.getId(), WRITER.getEmail());

        verify(commentRepository).save(COMMENT1);
    }

    @Test
    @DisplayName("댓글을 수정한다.")
    void update() {
        given(commentRepository.findById(EXIST_COMMENT_ID)).willReturn(Optional.of(updateComment));
        given(modelMapper.map(updateComment, CommentResponseDto.class)).willReturn(COMMENT_RESPONSE2);
        given(videoService.findById(EXIST_COMMENT_ID)).willReturn(VIDEO);
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
        given(videoService.findById(EXIST_VIDEO_ID)).willReturn(VIDEO);

        commentService.delete(EXIST_COMMENT_ID, WRITER.getEmail(), EXIST_VIDEO_ID);

        verify(commentRepository).deleteById(EXIST_COMMENT_ID);
    }

    @Test
    @DisplayName("없는 댓글을 삭제하는 경우 예외를 던진다.")
    void deleteFail() {
        assertThrows(NotFoundCommentException.class, () -> commentService.delete(NOT_EXIST_COMMENT_ID, WRITER.getEmail(), EXIST_VIDEO_ID));
    }
}
