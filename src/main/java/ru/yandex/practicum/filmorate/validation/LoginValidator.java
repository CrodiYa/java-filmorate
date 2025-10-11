package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator for {@link ValidLogin}
 */
public class LoginValidator implements ConstraintValidator<ValidLogin, String> {

    /**
     * Validates that the login is not null, blank, empty, or contains whitespaces.
     *
     * @param s                          the date to validate
     * @param constraintValidatorContext validation context
     * @return true if login is not null, blank, empty or contains whitespaces, false otherwise
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        return s != null && !s.isBlank() && s.chars().noneMatch(Character::isWhitespace);
    }
}
