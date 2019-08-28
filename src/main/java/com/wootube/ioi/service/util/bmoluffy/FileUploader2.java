package com.wootube.ioi.service.util.bmoluffy;

import com.wootube.ioi.service.util.UploadType;

import java.io.File;

public interface FileUploader2 {
    String uploadFile(File file, UploadType uploadType);

    void deleteFile(String fileName, UploadType uploadType);
}
