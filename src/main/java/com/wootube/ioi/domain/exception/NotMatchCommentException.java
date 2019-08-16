package com.wootube.ioi.domain.exception;

public class NotMatchCommentException extends RuntimeException {
    public NotMatchCommentException() {
        super("해당 답글과 일치하지 않는 댓글입니다.");
    }
}
