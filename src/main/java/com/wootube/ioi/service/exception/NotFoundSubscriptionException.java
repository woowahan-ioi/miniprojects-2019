package com.wootube.ioi.service.exception;

public class NotFoundSubscriptionException extends RuntimeException {
    public NotFoundSubscriptionException() {
        super("존재하지 않는 구독 관계입니다.");
    }
}
