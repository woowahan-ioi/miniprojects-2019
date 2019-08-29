package com.wootube.ioi.domain.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ProfileImage {

    public static final String DEFAULT_PROFILE_IMAGE_URL = "https://woowa-ioi.s3.ap-northeast-2.amazonaws.com/wootube/basic/default_profile.png";
    public static final String DEFAULT_PROFILE_IMAGE_FILE_NAME = "default_profile.png";

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false)
    private String profileImageFileName;

    public ProfileImage(String profileImageUrl, String profileImageFileName) {
        this.profileImageUrl = profileImageUrl;
        this.profileImageFileName = profileImageFileName;
    }

    public static ProfileImage defaultImage() {
        return new ProfileImage(DEFAULT_PROFILE_IMAGE_URL, DEFAULT_PROFILE_IMAGE_FILE_NAME);
    }

    public boolean isDefaultFileName() {
        return profileImageFileName.equals(DEFAULT_PROFILE_IMAGE_FILE_NAME);
    }
}
