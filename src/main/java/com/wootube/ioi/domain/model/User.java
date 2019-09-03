package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.ActivatedException;
import com.wootube.ioi.domain.exception.InactivatedException;
import com.wootube.ioi.domain.exception.NotMatchPasswordException;
import com.wootube.ioi.domain.validator.Password;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    @Pattern(regexp = "[^ \\-!@#$%^&*(),.?\":{}|<>0-9]{2,10}",
            message = "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.")
    @Column(nullable = false,
            length = 30)
    private String name;

    @Email(message = "이메일 양식 오류")
    @Column(nullable = false,
            length = 100,
            unique = true)
    private String email;

    @Column(nullable = false,
            length = 100)
    @Password(message = "비밀번호 양식 오류, 8-32자, 영문자 숫자 조합")
    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "profile_image_url")),
            @AttributeOverride(name = "fileName", column = @Column(name = "profile_image_file_name"))
    })
    private ProfileImage profileImage = ProfileImage.defaultImage();

    @Column(name = "active")
    private boolean active = true;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User matchPassword(String password) {
        if (!password.equals(this.password)) {
            throw new NotMatchPasswordException();
        }
        return this;
    }

    public User updateName(String name) {
        this.name = name;
        return this;
    }

    public void activateUser() {
        if (this.active) {
            throw new ActivatedException();
        }
        this.active = true;
    }

    public void softDelete() {
        if (!this.active) {
            throw new InactivatedException();
        }
        this.active = false;
    }

    public User updateProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public boolean isDefaultProfile() {
        return profileImage.isDefaultFileName();
    }
}
