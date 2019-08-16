package com.wootube.ioi.service.exception;

public class FileUploadException extends RuntimeException {
    public FileUploadException() {
        super("파일을 업로드하는데 실패했습니다. 다시 시도해주세요.");
    }
}
