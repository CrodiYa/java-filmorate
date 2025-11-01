package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.films.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

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
    public void shouldReturnSize() {
        storage.add(film);
        assertEquals(storage.getAll().size(), storage.size());
    }
}
