package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReleaseDateValidatorTest {
    private final ReleaseDateValidator validator = new ReleaseDateValidator();

    @Test
    public void shouldReturnTrueForValidDate() {
        LocalDate releaseDate = LocalDate.now();
        assertTrue(validator.isValid(releaseDate, null));
    }

    @Test
    public void shouldReturnFalseForImpossibleDate() {
        LocalDate impossibleRelease = LocalDate.of(1595, 1, 28);
        assertFalse(validator.isValid(impossibleRelease, null));
    }

    @Test
    public void shouldReturnTrueForNull() {
        assertTrue(validator.isValid(null, null));
    }
}
