package com.wootube.ioi.service.util;

import java.io.File;

public interface FileUploader {
    String uploadFile(File file, UploadType uploadType);

    void deleteFile(String fileName, UploadType uploadType);
}
