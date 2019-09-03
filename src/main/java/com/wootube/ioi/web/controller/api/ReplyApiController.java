package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.ReplyLikeService;
import com.wootube.ioi.service.ReplyService;
import com.wootube.ioi.service.dto.ReplyLikeResponseDto;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/videos/{videoId}/comments/{commentId}/replies")
@RestController
public class ReplyApiController {
    private static final Sort ASC_SORT_BY_UPDATE_TIME = new Sort(Sort.Direction.ASC, "updateTime");

    private final ReplyService replyService;
    private final ReplyLikeService replyLikeService;
    private final UserSessionManager userSessionManager;

    public ReplyApiController(ReplyService replyService, ReplyLikeService replyLikeService, UserSessionManager userSessionManager) {
        this.replyService = replyService;
        this.replyLikeService = replyLikeService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping("/sort/updatetime")
    public ResponseEntity<List<ReplyResponseDto>> sortReplyByUpdateTime(@PathVariable Long videoId,
                                                                        @PathVariable Long commentId) {
        List<ReplyResponseDto> replies = replyService.sortReply(ASC_SORT_BY_UPDATE_TIME, videoId, commentId);

        if (userSessionManager.getUserSession() == null) {
            replyLikeService.saveReplyLike(replies);
            return ResponseEntity.ok(replies);
        }

        UserSession userSession = userSessionManager.getUserSession();
        replyLikeService.saveReplyLike(replies, userSession.getId());
        return ResponseEntity.ok(replies);
    }


    @PostMapping
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long videoId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody ReplyRequestDto replyRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        ReplyResponseDto replyResponseDto = replyService.save(replyRequestDto, commentId, userSession.getEmail(), videoId);

        replyLikeService.saveReplyLike(replyResponseDto);

        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + replyResponseDto.getId()))
                .body(replyResponseDto);
    }

    @PostMapping("/{replyId}/likes")
    public ResponseEntity<ReplyLikeResponseDto> like(@PathVariable Long videoId,
                                                     @PathVariable Long commentId,
                                                     @PathVariable Long replyId) {
        UserSession userSession = userSessionManager.getUserSession();
        ReplyLikeResponseDto replyLikeResponseDto = replyLikeService.likeReply(userSession.getId(), commentId, replyId);

        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + replyId))
                .body(replyLikeResponseDto);
    }

    @PutMapping("/{replyId}")
    public ResponseEntity updateReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @PathVariable Long replyId,
                                      @RequestBody ReplyRequestDto replyRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        replyService.update(replyRequestDto, userSession.getEmail(), videoId, commentId, replyId);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{replyId}/likes")
    public ResponseEntity<ReplyLikeResponseDto> dislike(@PathVariable Long videoId,
                                                        @PathVariable Long commentId,
                                                        @PathVariable Long replyId) {
        UserSession userSession = userSessionManager.getUserSession();
        ReplyLikeResponseDto replyLikeResponseDto = replyLikeService.dislikeReply(userSession.getId(), commentId, replyId);

        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId))
                .body(replyLikeResponseDto);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity deleteReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @PathVariable Long replyId) {
        UserSession userSession = userSessionManager.getUserSession();
        replyService.delete(videoId, commentId, replyId, userSession.getEmail());

        return ResponseEntity.noContent()
                .build();
    }
}
