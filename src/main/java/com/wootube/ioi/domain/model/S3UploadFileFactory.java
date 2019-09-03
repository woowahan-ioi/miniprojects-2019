package com.wootube.ioi.domain.model;

import com.wootube.ioi.service.util.FileConverter;
import com.wootube.ioi.service.util.FileUploader;
import com.wootube.ioi.service.util.UploadType;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
public class S3UploadFileFactory {
    private final MultipartFile uploadFile;
    private final FileConverter fileConverter;
    private final FileUploader fileUploader;

    private String videoUrl;
    private String thumbnailUrl;
    private String originFileName;
    private String thumbnailFileName;

    public S3UploadFileFactory(MultipartFile uploadFile, FileConverter fileConverter, FileUploader fileUploader) {
        this.uploadFile = uploadFile;
        this.fileConverter = fileConverter;
        this.fileUploader = fileUploader;
    }

    public S3UploadFileFactory invoke() {
        File convertedVideo = fileConverter.convert(uploadFile);

        File convertedThumbnail = fileConverter.convert(convertedVideo);

        videoUrl = fileUploader.uploadFile(convertedVideo, UploadType.VIDEO);
        thumbnailUrl = fileUploader.uploadFile(convertedThumbnail, UploadType.THUMBNAIL);

        originFileName = convertedVideo.getName();
        thumbnailFileName = convertedThumbnail.getName();

        convertedVideo.delete();
        convertedThumbnail.delete();
        return this;
    }

}