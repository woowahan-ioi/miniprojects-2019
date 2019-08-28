package com.wootube.ioi.service.exception;

public class AlreadySubscribedException extends RuntimeException {
    public AlreadySubscribedException() {
        super("이미 구독 중입니다.");
    }
}
