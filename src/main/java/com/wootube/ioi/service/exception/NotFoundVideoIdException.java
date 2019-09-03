package com.wootube.ioi.service.exception;

public class NotFoundVideoIdException extends RuntimeException {
    public NotFoundVideoIdException() {
        super("해당 영상을 찾을 수 없습니다.");
    }
}
