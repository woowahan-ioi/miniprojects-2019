package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.ReplyService;
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
    private static final Sort DESC_SORT_BY_UPDATE_TIME = new Sort(Sort.Direction.DESC, "updateTime");

    private final ReplyService replyService;
    private final UserSessionManager userSessionManager;

    public ReplyApiController(ReplyService replyService, UserSessionManager userSessionManager) {
        this.replyService = replyService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping("/sort/updatetime")
    public ResponseEntity<List<ReplyResponseDto>> sortReplyByUpdateTime(@PathVariable Long videoId,
                                                                          @PathVariable Long commentId) {
        List<ReplyResponseDto> replies = replyService.sortReply(DESC_SORT_BY_UPDATE_TIME, videoId, commentId);

        return ResponseEntity.ok(replies);
    }


    @PostMapping
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @RequestBody ReplyRequestDto replyRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        ReplyResponseDto replyResponseDto = replyService.save(replyRequestDto, commentId, userSession.getEmail(), videoId);
        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + replyResponseDto.getId()))
                .body(replyResponseDto);
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
