package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Reply;
import com.wootube.ioi.domain.repository.ReplyRepository;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import com.wootube.ioi.service.testutil.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
public class ReplyServiceTest extends TestUtil {
    @Mock
    ReplyRepository replyRepository;

    @Mock
    CommentService commentService;

    @Mock
    UserService userService;

    @Mock
    VideoService videoService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    Reply updateReply;

    @InjectMocks
    ReplyService replyService;

    @Test
    @DisplayName("답글을 생성한다.")
    void save() {
        given(userService.findByEmail(WRITER.getEmail())).willReturn(WRITER);
        given(videoService.findById(VIDEO.getId())).willReturn(VIDEO);
        given(commentService.findById(COMMENT1.getId())).willReturn(COMMENT1);
        given(replyRepository.save(REPLY1)).willReturn(REPLY1);
        given(modelMapper.map(REPLY1, ReplyResponseDto.class)).willReturn(REPLY_RESPONSE_DTO1);

        replyService.save(REPLY_REQUEST_DTO1, COMMENT1.getId(), WRITER.getEmail(), VIDEO.getId());

        verify(replyRepository).save(REPLY1);
    }

    @Test
    @DisplayName("답글을 수정한다.")
    void update() {
        given(userService.findByEmail(WRITER.getEmail())).willReturn(WRITER);
        given(videoService.findById(VIDEO.getId())).willReturn(VIDEO);
        given(commentService.findById(COMMENT1.getId())).willReturn(COMMENT1);
        given(replyRepository.findById(REPLY1.getId())).willReturn(Optional.of(updateReply));
        given(modelMapper.map(REPLY1, ReplyResponseDto.class)).willReturn(REPLY_RESPONSE_DTO1);

        replyService.update(REPLY_REQUEST_DTO1, WRITER.getEmail(), VIDEO.getId(), COMMENT1.getId(), REPLY1.getId());

        verify(updateReply).update(WRITER, COMMENT1, REPLY_REQUEST_DTO1.getContents());
    }

    @Test
    @DisplayName("답글을 삭제한다.")
    void delete() {
        given(userService.findByEmail(WRITER.getEmail())).willReturn(WRITER);
        given(videoService.findById(VIDEO.getId())).willReturn(VIDEO);
        given(commentService.findById(COMMENT1.getId())).willReturn(COMMENT1);
        given(replyRepository.findById(REPLY1.getId())).willReturn(Optional.of(REPLY1));

        replyService.delete(VIDEO.getId(), COMMENT1.getId(), REPLY1.getId(), WRITER.getEmail());

        verify(replyRepository).delete(REPLY1);
    }
}
