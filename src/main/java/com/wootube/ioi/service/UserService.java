package com.wootube.ioi.service;

import com.wootube.ioi.domain.exception.NotMatchPasswordException;
import com.wootube.ioi.domain.model.ProfileImage;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.EditUserRequestDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.*;
import com.wootube.ioi.service.util.FileConverter;
import com.wootube.ioi.service.util.FileUploader;
import com.wootube.ioi.service.util.UploadType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;

@Service
public class UserService {
    private static final String IMAGE_CONTENT_TYPE = "image";

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerifyKeyService verifyKeyService;
    private final ModelMapper modelMapper;
    private final FileUploader fileUploader;
    private final FileConverter fileConverter;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService, VerifyKeyService verifyKeyService, ModelMapper modelMapper, FileUploader fileUploader, FileConverter fileConverter) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.verifyKeyService = verifyKeyService;
        this.modelMapper = modelMapper;
        this.fileUploader = fileUploader;
        this.fileConverter = fileConverter;
    }

    public User createUser(SignUpRequestDto signUpRequestDto) {
        return userRepository.save(modelMapper.map(signUpRequestDto, User.class));
    }

    public User readUser(LogInRequestDto logInRequestDto) {
        try {
            User savedEmail = findByEmail(logInRequestDto.getEmail());
            checkInActive(savedEmail);
            return savedEmail.matchPassword(logInRequestDto.getPassword());
        } catch (NotFoundUserException | NotMatchPasswordException e) {
            throw new LoginFailedException();
        }
    }

    private void checkInActive(User savedEmail) {
        if (!savedEmail.isActive()) {
            emailService.sendMessage(savedEmail.getEmail());
            throw new InActivatedUserException();
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public User updateUser(Long userId, EditUserRequestDto editUserRequestDto) {
        return findByIdAndIsActiveTrue(userId).updateName(editUserRequestDto.getName());
    }

    public User findByIdAndIsActiveTrue(Long userId) {
        return userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public User deleteUser(Long userId) {
        User deleteTargetUser = findByIdAndIsActiveTrue(userId);
        deleteTargetUser.softDelete();
        return deleteTargetUser;
    }

    @Transactional
    public void activateUser(String email, String verifyKey) {
        if (verifyKeyService.confirmKey(email, verifyKey)) {
            findByEmail(email).activateUser();
        }
    }

    @Transactional
    public User updateProfileImage(Long userId, MultipartFile uploadFile) throws IOException {
        checkMediaType(uploadFile);

        User user = findByIdAndIsActiveTrue(userId);

        ProfileImage profileImage = user.getProfileImage();
        if (!user.isDefaultProfile()) {
            fileUploader.deleteFile(profileImage.getProfileImageFileName(), UploadType.PROFILE);
        }

        File convertedProfileImage = fileConverter.convert(uploadFile)
                .orElseThrow(FileConvertException::new);

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
