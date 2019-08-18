package com.wootube.ioi.service.exception;

public class NotFoundReplyException extends RuntimeException {
    public NotFoundReplyException() {
        super("존재하지 않는 답글 입니다.");
    }
}
