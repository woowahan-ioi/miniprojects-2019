package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentRequest;
import com.wootube.ioi.service.dto.CommentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CommentServiceTest {
    private static final Long EXIST_COMMENT_ID = 1L;
    private static final Long NOT_EXIST_COMMENT_ID = 0L;

    private static final String COMMENT1_CONTENTS = "Comment Contents 1";
    private static final String COMMENT2_CONTENTS = "Comment Contents 2";
    private static final String COMMENT3_CONTENTS = "Comment Contents 3";

    private static final CommentRequest COMMENT_REQUEST1 = new CommentRequest(COMMENT1_CONTENTS);
    private static final CommentRequest COMMENT_REQUEST2 = new CommentRequest(COMMENT2_CONTENTS);
    private static final CommentRequest COMMENT_REQUEST3 = new CommentRequest(COMMENT3_CONTENTS);

    private static final Comment COMMENT1 = new Comment(COMMENT_REQUEST1.getContents());
    private static final Comment COMMENT2 = new Comment(COMMENT_REQUEST2.getContents());
    private static final Comment COMMENT3 = new Comment(COMMENT_REQUEST3.getContents());

    private static final CommentResponse COMMENT_RESPONSE1 = new CommentResponse(EXIST_COMMENT_ID,
            "Comment Contents 1",
            LocalDateTime.now());
    private static final CommentResponse COMMENT_RESPONSE2 = new CommentResponse(EXIST_COMMENT_ID,
            "Comment Contents 2",
            LocalDateTime.now());
    private static final CommentResponse COMMENT_RESPONSE3 = new CommentResponse(EXIST_COMMENT_ID,
            "Comment Contents 3",
            LocalDateTime.now());


    @Mock
    private CommentRepository commentRepository;

    @Mock
    private Comment updateComment;

    @InjectMocks
    private CommentService commentService;

    @Test
    void save() {
        given(commentRepository.save(COMMENT1)).willReturn(COMMENT1);

        commentService.save(COMMENT_REQUEST1);

        verify(commentRepository).save(COMMENT1);
    }

    @Test
    void update() {
        given(commentRepository.findById(1L)).willReturn(Optional.of(updateComment));

        commentService.update(1L, COMMENT_REQUEST2);

        verify(updateComment).update(COMMENT_REQUEST2.getContents());
    }

}
