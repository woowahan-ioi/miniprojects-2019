package com.wootube.ioi.service.exception;

public class NotMatchVerifyKeyException extends RuntimeException {
    public NotMatchVerifyKeyException() {
        super("verifyKey가 일치하지 않습니다.");
    }
}
