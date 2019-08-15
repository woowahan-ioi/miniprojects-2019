package com.wootube.ioi.domain.validator;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.*;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    public void initialize(Password constraint) {
    }

    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 32),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Alphabetical, 1),
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
