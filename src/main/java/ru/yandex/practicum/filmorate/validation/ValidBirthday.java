package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a birthdate is not in the future.
 *
 * <p>Applied to {@code LocalDate} fields to ensure the date represents a past or present date.
 * Used primarily for user birthdate validation.</p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthdayValidator.class)
public @interface ValidBirthday {
    String message() default "Дата рождения не может быть в будущем";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}