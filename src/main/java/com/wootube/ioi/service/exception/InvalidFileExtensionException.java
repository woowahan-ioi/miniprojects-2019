package com.wootube.ioi.service.exception;

public class InvalidFileExtensionException extends RuntimeException {
    public InvalidFileExtensionException() {
        super("파일 형식이 맞지 않습니다.");
    }
}
