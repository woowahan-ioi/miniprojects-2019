package com.wootube.ioi.domain.exception;

public class NotMatchWriterException extends RuntimeException {
    public NotMatchWriterException() {
        super("해당 유저는 댓글을 작성한 유저가 아닙니다.");
    }
}
