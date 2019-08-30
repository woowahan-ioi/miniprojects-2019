package com.wootube.ioi.service.util;

import lombok.Getter;

@Getter
public enum UploadType {
    VIDEO("video"),
    PROFILE("profile"),
    THUMBNAIL("thumbnail");

    private final String uploadType;

    UploadType(String uploadType) {
        this.uploadType = uploadType;
    }
}

