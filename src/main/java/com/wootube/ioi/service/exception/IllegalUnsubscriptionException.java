package com.wootube.ioi.service.exception;

public class IllegalUnsubscriptionException extends RuntimeException {
    public IllegalUnsubscriptionException() {
        super("자기 자신을 구독 취소할 수 없습니다.");
    }
}
