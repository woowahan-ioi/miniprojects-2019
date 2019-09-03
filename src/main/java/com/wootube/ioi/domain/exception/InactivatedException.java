package com.wootube.ioi.domain.exception;

public class InactivatedException extends RuntimeException {
    public InactivatedException() {
        super("이미 삭제된 데이터입니다.");
    }
}
