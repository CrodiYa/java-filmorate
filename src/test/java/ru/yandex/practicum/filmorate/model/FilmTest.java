package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest extends ModelTest<Film> {
    private Film film;

    @BeforeEach
    public void initFilm() {
        film = new Film(1L, "name", "description", LocalDate.now(), 10);
    }

    @Nested
    class FilmNameTest {

        @Test
        public void shouldFindViolationWhenNameIsNull() {
            film.setName(null);
            assertEquals(1, validateModel(film));
        }

        @Test
        public void shouldFindViolationWhenNameIsEmpty() {
            film.setName("");
            assertEquals(1, validateModel(film));
        }

        @Test
        public void shouldFindViolationWhenNameIsBlank() {
            film.setName("  ");
            assertEquals(1, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenNameIsValid() {
            film.setName("ihateunittests");
            assertEquals(0, validateModel(film));
        }
    }

    @Nested
    class FilmDescriptionTest {

        @Test
        public void shouldFindViolationWhenDescriptionIsOver200chars() {
            film.setDescription("a".repeat(250));
            assertEquals(1, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDescriptionIs200chars() {
            film.setDescription("a".repeat(200));
            assertEquals(0, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDescriptionIsValid() {
            film.setDescription("valid");
            assertEquals(0, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDescriptionIsNull() {
            film.setDescription(null);
            assertEquals(0, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDescriptionIsEmpty() {
            film.setDescription("");
            assertEquals(0, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDescriptionIsBlank() {
            film.setDescription("  ");
            assertEquals(0, validateModel(film));
        }
    }

    @Nested
    class FilmReleaseDateTest {
        @Test
        public void shouldFindViolationWhenReleaseDateBefore() {
            film.setReleaseDate(LocalDate.of(1000, 1, 1));
            assertEquals(1, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenReleaseDateValid() {
            film.setReleaseDate(LocalDate.now());
            assertEquals(0, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDescriptionIsNull() {
            film.setReleaseDate(null);
            assertEquals(0, validateModel(film));
        }
    }

    @Nested
    class FilmDurationTest {
        @Test
        public void shouldFindViolationWhenDurationIsZero() {
            film.setDuration(0);
            assertEquals(1, validateModel(film));
        }

        @Test
        public void shouldFindViolationWhenDurationIsNegative() {
            film.setDuration(-1);
            assertEquals(1, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDurationIsPositive() {
            film.setDuration(1);
            assertEquals(0, validateModel(film));
        }

        @Test
        public void shouldNotFindViolationWhenDurationIsNull() {
            film.setDuration(null);
            assertEquals(0, validateModel(film));
        }
    }
}