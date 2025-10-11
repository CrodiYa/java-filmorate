package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a releaseDate is not before 1985-01-28.
 *
 * <p>Applied to {@code LocalDate} fields to validate data
 * Used primarily for user Film`s releaseDate validation.</p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ValidReleaseDate {
    String message() default "Дата релиза не может быть раньше 28 декабря 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}