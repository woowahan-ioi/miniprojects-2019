package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.CommentLikeService;
import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentLikeResponseDto;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/videos/{videoId}/comments")
@RestController
public class CommentApiController {
    private static final Sort DESC_SORT_BY_UPDATE_TIME = new Sort(Sort.Direction.DESC, "updateTime");
    private static final Sort ASC_SORT_BY_UPDATE_TIME = new Sort(Sort.Direction.ASC, "updateTime");

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final UserSessionManager userSessionManager;

    public CommentApiController(CommentService commentService, CommentLikeService commentLikeService, UserSessionManager userSessionManager) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping("/sort/updatetime")
    public ResponseEntity<List<CommentResponseDto>> sortCommentByUpdateTime(@PathVariable Long videoId) {
        List<CommentResponseDto> comments = commentService.sortComment(ASC_SORT_BY_UPDATE_TIME, videoId);

        saveCommentLikeByUserSession(comments);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/sort/likecount")
    public ResponseEntity<List<CommentResponseDto>> sortCommentByLikeCount(@PathVariable Long videoId) {
        List<CommentResponseDto> comments = commentService.sortComment(DESC_SORT_BY_UPDATE_TIME, videoId);

        saveCommentLikeByUserSession(comments);
        return ResponseEntity.ok(comments);
    }

    private void saveCommentLikeByUserSession(List<CommentResponseDto> comments) {
        if (userSessionManager.getUserSession() != null) {
            commentLikeService.saveCommentLike(comments, userSessionManager.getUserSession().getId());
            return;
        }
        commentLikeService.saveCommentLike(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long videoId,
                                                            @RequestBody CommentRequestDto commentRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        CommentResponseDto commentResponseDto = commentService.save(commentRequestDto, videoId, userSession.getEmail());

        commentLikeService.saveCommentLike(commentResponseDto);

        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentResponseDto.getId()))
                .body(commentResponseDto);
    }

    @PostMapping("/{commentId}/likes")
    public ResponseEntity<CommentLikeResponseDto> like(@PathVariable Long videoId,
                                                       @PathVariable Long commentId) {
        UserSession userSession = userSessionManager.getUserSession();
        CommentLikeResponseDto commentLikeResponseDto = commentLikeService.likeComment(userSession.getId(), videoId, commentId);

        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId))
                .body(commentLikeResponseDto);
    }

    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<CommentLikeResponseDto> dislike(@PathVariable Long videoId,
                                                          @PathVariable Long commentId) {
        UserSession userSession = userSessionManager.getUserSession();
        CommentLikeResponseDto commentLikeResponseDto = commentLikeService.dislikeComment(userSession.getId(), commentId, videoId);

        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId))
                .body(commentLikeResponseDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long videoId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        commentService.update(commentId, userSession.getEmail(), videoId, commentRequestDto);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long videoId, @PathVariable Long commentId) {
        UserSession userSession = userSessionManager.getUserSession();
        commentService.delete(commentId, userSession.getEmail(), videoId);
        return ResponseEntity.noContent()
                .build();
    }
}
