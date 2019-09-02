package com.wootube.ioi.service.exception;

public class DescriptionMaxLengthException extends RuntimeException {
    public DescriptionMaxLengthException() {
        super("Desception 의 글자수는 1000자 내외 입니다.");
    }
}
