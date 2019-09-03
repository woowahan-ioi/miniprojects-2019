package com.wootube.ioi.domain.exception;

public class NotMatchVideoException extends RuntimeException {
    public NotMatchVideoException() {
        super("해당 영상과 맞지 않습니다.");
    }
}
