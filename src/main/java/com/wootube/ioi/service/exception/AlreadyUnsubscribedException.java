package com.wootube.ioi.service.exception;

public class AlreadyUnsubscribedException extends RuntimeException {
    public AlreadyUnsubscribedException() {
        super("구독하고 있지 않습니다.");
    }
}
