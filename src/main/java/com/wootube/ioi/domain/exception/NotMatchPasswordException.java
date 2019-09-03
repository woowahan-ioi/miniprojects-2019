package com.wootube.ioi.domain.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
