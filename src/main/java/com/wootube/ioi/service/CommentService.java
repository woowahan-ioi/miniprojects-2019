package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentRequest;
import com.wootube.ioi.service.dto.CommentResponse;
import com.wootube.ioi.service.exception.NotFoundCommentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponse save(CommentRequest commentRequest) {
        Comment comment = new Comment(commentRequest.getContents());
        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponse(updatedComment.getId(),
                updatedComment.getContents(),
                updatedComment.getUpdateTime());
    }

    @Transactional
    //public CommentResponse update(Long videoId, Long commentId, User sessionUser. CommentRequest commentRequest)
    public CommentResponse update(Long commentId, CommentRequest commentRequest) {
        // 같은 비디오인지 확인
        // Video video = videoService.findById(videoId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);

        //comment.update(video, sessionUser, contents);
        comment.update(commentRequest.getContents());
        return new CommentResponse(comment.getId(),
                comment.getContents(),
                comment.getUpdateTime());
    }

    //public void delete(Long videoId, Long commentId, User sessionUser)
    public void delete(Long commentId) {
        // 같은 비디오인지 확인
        // 세션 유저와 comment의 유저가 같은지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
//        if(comment.isSameWriteWith(sessionUser)){
            commentRepository.deleteById(commentId);
//        }
//        throw new NotMatchCommentWriterException();
    }
}
