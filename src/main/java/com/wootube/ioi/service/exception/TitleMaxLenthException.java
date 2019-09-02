package com.wootube.ioi.service.exception;

public class TitleMaxLenthException extends RuntimeException {
    public TitleMaxLenthException() {
        super("Title의 글자수는 50자 내외 입니다.");
    }
}
