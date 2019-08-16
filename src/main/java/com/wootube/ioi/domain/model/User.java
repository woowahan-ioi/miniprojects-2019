package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.validator.Password;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@EqualsAndHashCode(exclude = {"name", "email", "password"})
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
}
