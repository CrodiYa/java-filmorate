package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginValidatorTest {

    private final LoginValidator validator = new LoginValidator();

    @Test
    public void shouldReturnTrueForValidLogin() {
        String login = "login";
        assertTrue(validator.isValid(login, null));
    }

    @Test
    public void shouldReturnFalseWhenLoginNull() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    public void shouldReturnFalseWhenLoginEmpty() {
        assertFalse(validator.isValid("", null));
    }

    @Test
    public void shouldReturnFalseWhenLoginBlank() {
        assertFalse(validator.isValid(" ", null));
    }

    @Test
    public void shouldReturnFalseWhenLoginContainsWhiteSpaces() {
        assertFalse(validator.isValid("some login", null));
    }

    @Test
    public void shouldReturnFalseWhenLoginContainsLotsOfWhiteSpaces() {
        assertFalse(validator.isValid("some   log    in    ", null));
    }
}