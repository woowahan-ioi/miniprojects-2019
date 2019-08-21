package com.wootube.ioi.service.exception;

public class InActivatedUserException extends RuntimeException {
    public InActivatedUserException() {
        super("비활성화된 사용자 입니다. 메일을 확인해주세요.");
    }
}
