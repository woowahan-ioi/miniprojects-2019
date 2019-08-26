package com.wootube.ioi.domain.validator;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 32;
    private static final int MINIMUM_CONTAIN = 1;

    public void initialize(Password constraint) {
    }

    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(MIN_LENGTH, MAX_LENGTH),
                new CharacterRule(EnglishCharacterData.Digit, MINIMUM_CONTAIN),
                new CharacterRule(EnglishCharacterData.Alphabetical, MINIMUM_CONTAIN),
                new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);

        context.buildConstraintViolationWithTemplate(String.join(",", messages))
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}
