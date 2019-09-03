package com.wootube.ioi.domain.model;

import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class VideoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("비디오 컨텐트 경로 및 오리지널 파일 이름 초기화 테스트")
    void initializeVideo() {
        Video testVideo = new Video("title", "description");
        testVideo.initialize("contentPath", "thumbnailPath", "originFileName.mp4", "thumbnailFileName.png", new User());
        assertNoViolation(testVideo).isTrue();
    }

    private AbstractBooleanAssert<?> assertNoViolation(Video video) {
        Set<ConstraintViolation<Video>> violations = validator.validate(video);
        return AssertionsForClassTypes.assertThat(violations.isEmpty());
    }

    @Test
    @DisplayName("비디오 업데이트 테스트")
    void update() {
        Video testVideo = new Video("title", "description");
        testVideo.initialize("contentPath", "thumbnailPath", "originFileName.mp4", "thumbnailFileName.png", new User());

        Video updateTestVideo = new Video("update_title", "update_desc");
        testVideo.update(updateTestVideo);

        assertThat(testVideo.getTitle()).isEqualTo(updateTestVideo.getTitle());
        assertThat(testVideo.getDescription()).isEqualTo(updateTestVideo.getDescription());
    }

}