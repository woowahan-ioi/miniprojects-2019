package com.wootube.ioi.service.exception;

public class InvalidFileExtensionException extends RuntimeException {
    public InvalidFileExtensionException() {
        super("파일형식이 맞지 않습니다. 동영상 파일만 올려주세요.");
    }
}
