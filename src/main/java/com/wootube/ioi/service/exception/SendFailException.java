package com.wootube.ioi.service.exception;

public class SendFailException extends RuntimeException {
    public SendFailException() {
        super("알 수 없는 에러가 발생했습니다. Wootube에 문의하세요.");
    }
}
