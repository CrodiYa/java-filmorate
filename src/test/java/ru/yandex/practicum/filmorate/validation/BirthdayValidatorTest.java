package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BirthdayValidatorTest {
    private final BirthdayValidator validator = new BirthdayValidator();

    @Test
    public void shouldReturnTrueForValidDate() {
        LocalDate birthday = LocalDate.of(1595, 1, 28);
        assertTrue(validator.isValid(birthday, null));
    }

    @Test
    public void shouldReturnTrueForToday() {
        LocalDate future = LocalDate.now();
        assertTrue(validator.isValid(future, null));
    }

    @Test
    public void shouldReturnFalseForFutureDate() {
        LocalDate future = LocalDate.now().plusDays(1);
        assertFalse(validator.isValid(future, null));
    }

    @Test
    public void shouldReturnTrueForNull() {
        assertTrue(validator.isValid(null, null));
    }
}