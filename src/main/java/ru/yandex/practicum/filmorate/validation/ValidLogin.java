package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a login has no whitespaces.
 *
 * <p>Applied to {@code String} fields to ensure the data is not null, blank and has not whitespaces
 * Used primarily for user login validation.</p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface ValidLogin {
    String message() default "Логин не может быть пустым и не должен содержать пробелы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}