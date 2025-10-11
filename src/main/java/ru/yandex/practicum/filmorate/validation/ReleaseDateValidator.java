package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Custom validator for {@link ValidReleaseDate}
 */
public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {

    private static final LocalDate MIN_ALLOWED_DATE = LocalDate.of(1895, 1, 28);

    /**
     * Validates that the release date is not before 1895-01-28.
     *
     * @param localDate the date to validate
     * @param constraintValidatorContext validation context
     * @return true if date is null or after minimum allowed date, false otherwise
     */
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return true;
        }

        return !localDate.isBefore(MIN_ALLOWED_DATE);
    }
}
