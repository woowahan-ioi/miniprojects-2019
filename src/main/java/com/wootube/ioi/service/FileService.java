package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.ProfileImage;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.exception.InvalidFileExtensionException;
import com.wootube.ioi.service.util.FileConverter;
import com.wootube.ioi.service.util.FileUploader;
import com.wootube.ioi.service.util.UploadType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private static final String IMAGE_CONTENT_TYPE = "image";

    private UserService userService;
    private FileUploader fileUploader;
    private FileConverter fileConverter;

    @Autowired
    public FileService(UserService userService, FileUploader fileUploader, FileConverter fileConverter) {
        this.userService = userService;
        this.fileUploader = fileUploader;
        this.fileConverter = fileConverter;
    }

    @Transactional
    public User updateProfileImage(Long userId, MultipartFile uploadFile) throws IOException {
        checkMediaType(uploadFile);

        User user = userService.findByIdAndIsActiveTrue(userId);

        ProfileImage profileImage = user.getProfileImage();

        if (!user.isDefaultProfile()) {
            fileUploader.deleteFile(profileImage.getProfileImageFileName(), UploadType.PROFILE);
        }

        File convertedProfileImage = fileConverter.convert(uploadFile);

        String profileImageUrl = fileUploader.uploadFile(convertedProfileImage, UploadType.PROFILE);
        String originFileName = uploadFile.getOriginalFilename();

        convertedProfileImage.delete();

        return user.updateProfileImage(new ProfileImage(profileImageUrl, originFileName));
    }

    private void checkMediaType(MultipartFile uploadFile) {
        if (StringUtils.startsWith(uploadFile.getContentType(), IMAGE_CONTENT_TYPE)) {
            return;
        }
        throw new InvalidFileExtensionException();
    }
}
