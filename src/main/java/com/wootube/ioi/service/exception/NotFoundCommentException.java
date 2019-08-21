package com.wootube.ioi.service.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
        super("존재하지 않는 댓글 입니다.");
    }
}
