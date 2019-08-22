package com.wootube.ioi.service.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("로그인 하지 않았습니다.");
    }
}
