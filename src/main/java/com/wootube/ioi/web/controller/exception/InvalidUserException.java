package com.wootube.ioi.web.controller.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("유효하지 않은 사용자입니다.");
    }
}
