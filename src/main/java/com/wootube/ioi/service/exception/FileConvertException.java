package com.wootube.ioi.service.exception;

public class FileConvertException extends RuntimeException {
    public FileConvertException() {
        super("파일 변환에 실패했습니다.");
    }
}
