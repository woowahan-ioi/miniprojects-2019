package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchPasswordException;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static final String VALID_NAME = "루피";
    private static final String VALID_EMAIL = "luffy@luffy.com";
    private static final String VALID_PASSWORD = "1234567a";

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("모든 Validation 정상 통과")
    @Test
    void validUser() {
        User user = new User(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);
        assertNoViolation(user).isTrue();
    }

    private AbstractBooleanAssert<?> assertNoViolation(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        return AssertionsForClassTypes.assertThat(violations.isEmpty());
    }

    @DisplayName("이름 Validation 오류, 숫자 포함")
    @Test
    void invalidNameWithNumeric() {
        User user = new User("루피1", VALID_EMAIL, VALID_PASSWORD);
        assertContainViolation(user, "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.").isTrue();
    }

    private AbstractBooleanAssert<?> assertContainViolation(User user, String message) {
        return AssertionsForClassTypes.assertThat(
                validator.validate(user)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList())
                        .contains(message)
        );
    }

    @DisplayName("이름 Validation 오류, 특수문자 포함")
    @Test
    void invalidNameWithSymbolic() {
        User user = new User("루피-", VALID_EMAIL, VALID_PASSWORD);
        assertContainViolation(user, "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.").isTrue();
    }

    @DisplayName("이메일 Validation 오류, 이메일 양식 오류")
    @Test
    void invalidEmail() {
        User user = new User(VALID_NAME, "luffy", VALID_PASSWORD);
        assertContainViolation(user, "이메일 양식 오류").isTrue();
    }

    @DisplayName("비밀번호 Validation 오류, 숫자 없는 경우")
    @Test
    void invalidPasswordNoNumeric() {
        User user = new User(VALID_NAME, VALID_EMAIL, "abcdefgh");
        assertContainViolation(user, "비밀번호 양식 오류, 8-32자, 영문자 숫자 조합");
    }

    @DisplayName("비밀번호 Validation 오류, 알파벳 없는 경우")
    @Test
    void invalidPasswordNoAlphabet() {
        User user = new User(VALID_NAME, VALID_EMAIL, "12345678");
        assertContainViolation(user, "비밀번호 양식 오류, 8-32자, 영문자 숫자 조합");
    }

    @DisplayName("비밀번호 일치 확인")
    @Test
    void matchPassword() {
        User user = new User(VALID_NAME, VALID_NAME, VALID_PASSWORD);

        assertDoesNotThrow(() -> user.matchPassword(VALID_PASSWORD));
    }

    @DisplayName("비밀번호 불일치 예외")
    @Test
    void notMatchPassword() {
        User user = new User(VALID_NAME, VALID_NAME, VALID_PASSWORD);
        String notMatchPassword = "aaaa1234";

        assertThrows(NotMatchPasswordException.class, () -> user.matchPassword(notMatchPassword));
    }

    @DisplayName("이름 업데이트")
    @Test
    void updateName() {
        String newName = "샹크스";
        User user = new User(VALID_NAME, VALID_NAME, VALID_PASSWORD);
        User updatedUser = user.updateName(newName);
        assertEquals(updatedUser.getName(), newName);
    }
}