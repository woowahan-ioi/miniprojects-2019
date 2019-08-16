package com.wootube.ioi.domain.exception;

public class NotMatchCommentException extends RuntimeException {
    public NotMatchCommentException() {
        super("댓글에 일치하지 않은 답글 입니다.");
    }
}
