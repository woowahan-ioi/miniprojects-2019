package com.wootube.ioi.service.util;

import lombok.Getter;

@Getter
public enum UploadType {
    VIDEO("video"),
    PROFILE("profile");

    private final String uploadType;

    UploadType(String uploadType) {
        this.uploadType = uploadType;
    }
}

