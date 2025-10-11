package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest extends ModelTest<User> {

    private User user;

    @BeforeEach
    public void initUser() {
        user = new User(1L,
                "valid@mail.com",
                "login", "name",
                LocalDate.of(2000, 1, 1));
    }

    @Nested
    class UserEmailTest {

        @Test
        public void shouldFindViolationWhenEmailIsNull() {
            user.setEmail(null);
            assertEquals(1, validateModel(user));
        }

        @Test
        public void shouldFindViolationWhenEmailIsEmpty() {
            user.setEmail("");
            assertEquals(1, validateModel(user));
        }

        @Test
        public void shouldFindViolationWhenEmailIsBlank() {
            user.setEmail("  ");
            // 2 since it not valid email and blank
            assertEquals(2, validateModel(user));
        }

        @Test
        public void shouldFindViolationWhenEmailIsNotValid() {
            user.setEmail("@email");
            assertEquals(1, validateModel(user));
        }

        @Test
        public void shouldNotFindViolationWhenEmailIsValid() {
            user.setEmail("valid@mail.com");
            assertEquals(0, validateModel(user));
        }
    }

    @Nested
    class UserLoginTest {

        @Test
        public void shouldFindViolationWhenNameIsNull() {
            user.setLogin(null);
            assertEquals(1, validateModel(user));
        }

        @Test
        public void shouldFindViolationWhenNameIsEmpty() {
            user.setLogin("");
            assertEquals(1, validateModel(user));
        }

        @Test
        public void shouldFindViolationWhenNameIsBlank() {
            user.setLogin("  ");
            assertEquals(1, validateModel(user));
        }

        @Test
        public void shouldNotFindViolationWhenNameIsValid() {
            user.setLogin("ihateunittests");
            assertEquals(0, validateModel(user));
        }
    }

    @Nested
    class UserBirthdayTest {

        @Test
        public void shouldFindViolationWhenBirthdayIsFuture() {
            user.setBirthday(LocalDate.now().plusDays(1));
            assertEquals(1, validateModel(user));
        }

        @Test
        public void shouldNotFindViolationWhenBirthdayIsToday() {
            user.setBirthday(LocalDate.now());
            assertEquals(0, validateModel(user));
        }

        @Test
        public void shouldNotFindViolationWhenBirthdayIsValid() {
            user.setBirthday(LocalDate.of(2000, 1, 1));
            assertEquals(0, validateModel(user));
        }

        @Test
        public void shouldNotFindViolationWhenBirthdayIsNull() {
            user.setBirthday(null);
            assertEquals(0, validateModel(user));
        }
    }
}