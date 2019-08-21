package com.wootube.ioi.service.exception;

public class UserAndWriterMisMatchException extends RuntimeException {
    public UserAndWriterMisMatchException() {
        super("현재 로그인 한 유저와 비디오 작성자의 이메일이 맞지 않습니다.");
    }
}
