package com.wootube.ioi.service.exception;

public class NotFoundVideoException extends RuntimeException {
    public NotFoundVideoException() {
        super("해당 영상을 찾을 수 없습니다.");
    }
}
