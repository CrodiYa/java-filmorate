package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.film.memory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmStorageTest {

    private FilmStorage storage;
    private Film film;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryFilmStorage();
        film = new Film();
        film.setId(1L);
    }

    @Test
    public void shouldAddObject() {
        Film user1 = storage.add(film);

        assertEquals(1L, user1.getId());
        assertEquals(film, user1);
    }

    @Test
    public void shouldUpdateObject() {
        storage.add(film);

        Film user1 = storage.update(film);

        assertEquals(1L, user1.getId());
        assertEquals(film, user1);
    }

    @Test
    public void shouldRemoveObject() {
        storage.add(film);

        Film user1 = storage.remove(1L);

        assertEquals(1L, user1.getId());
        assertEquals(film, user1);
        assertEquals(0, storage.getAll().size());
    }

    @Test
    public void shouldGetObject() {
        storage.add(film);
        Film user1 = storage.get(1L);

        assertEquals(1L, user1.getId());
        assertEquals(film, user1);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGetObjectThatDoesNotExist() {
        assertNull(storage.get(1L));
    }

    @Test
    public void shouldReturnTrueIfContains() {
        storage.add(film);
        assertTrue(storage.contains(1L));
    }

    @Test
    public void shouldReturnFalseIfNotContains() {
        assertFalse(storage.contains(1L));
    }

    @Test
    public void shouldReturnTopFilms() {
        Film film1 = new Film(1L, "Film 1", "Desc 1", LocalDate.now(), 120);
        film1.setLikes(10L);

        Film film2 = new Film(2L, "Film 2", "Desc 2", LocalDate.now(), 130);
        film2.setLikes(5L);

        Film film3 = new Film(3L, "Film 3", "Desc 3", LocalDate.now(), 140);
        film3.setLikes(15L);

        storage.add(film1);
        storage.add(film2);
        storage.add(film3);

        List<Film> top = (List<Film>) storage.getTopFilms(5L);

        assertEquals(3,top.size());
        assertEquals(film3, top.get(0));
        assertEquals(film1, top.get(1));
        assertEquals(film2, top.get(2));
    }

    @Test
    public void shouldReturnOnlyTwoTopFilms() {
        Film film1 = new Film(1L, "Film 1", "Desc 1", LocalDate.now(), 120);
        film1.setLikes(10L);

        Film film2 = new Film(2L, "Film 2", "Desc 2", LocalDate.now(), 130);
        film2.setLikes(5L);

        Film film3 = new Film(3L, "Film 3", "Desc 3", LocalDate.now(), 140);
        film3.setLikes(15L);

        storage.add(film1);
        storage.add(film2);
        storage.add(film3);

        List<Film> top = (List<Film>) storage.getTopFilms(2L);

        assertEquals(2,top.size());
        assertEquals(film3, top.get(0));
        assertEquals(film1, top.get(1));
    }
}
