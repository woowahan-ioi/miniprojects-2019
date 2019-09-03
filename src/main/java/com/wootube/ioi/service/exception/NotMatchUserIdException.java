package com.wootube.ioi.service.exception;

public class NotMatchUserIdException extends RuntimeException {
    public NotMatchUserIdException() {
        super("사용자가 일치하지 않습니다.");
    }
}
