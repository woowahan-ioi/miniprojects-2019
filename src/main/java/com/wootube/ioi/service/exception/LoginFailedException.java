package com.wootube.ioi.service.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("로그인 정보를 확인해주세요.");
    }
}
