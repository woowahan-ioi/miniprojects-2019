package com.wootube.ioi.domain.exception;

public class ActivatedException extends RuntimeException {
    public ActivatedException() {
        super("이미 활성화된 사용자입니다.");
    }
}
