package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Custom validator for {@link ValidBirthday}
 */
public class BirthdayValidator implements ConstraintValidator<ValidBirthday, LocalDate> {

    /**
     * Validates that the birthday is not in the future.
     *
     * @param localDate the date to validate
     * @param constraintValidatorContext validation context
     * @return true if date is null or in past or present, false otherwise
     */
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return true;
        }

        return !localDate.isAfter(LocalDate.now());
    }
}
