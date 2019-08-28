package com.wootube.ioi.domain.exception;

public class IllegalSubscriptionException extends RuntimeException {
    public IllegalSubscriptionException() {
        super("자기 자신을 구독할 수 없습니다.");
    }
}
