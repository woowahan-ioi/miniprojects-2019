package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchPasswordException;
import com.wootube.ioi.domain.validator.Password;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
