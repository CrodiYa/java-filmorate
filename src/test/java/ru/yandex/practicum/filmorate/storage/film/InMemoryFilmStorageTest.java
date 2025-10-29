package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {

    private FilmStorage storage;
    private Film film;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryFilmStorage();
        film = new Film();
    }

    private Map<Long, Set<Long>> getLikesField() {

        try {
            Field field = storage.getClass().getDeclaredField("likes");
            field.setAccessible(true);
            return (Map<Long, Set<Long>>) field.get(storage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldAddFilmAndCreateLikesSet() {
        Film user1 = storage.add(film);

        assertEquals(1L, user1.getId());
        assertEquals(film, user1);
        assertNotNull(getLikesField().get(1L));
    }

    @Test
    public void shouldRemoveFilmAndDeleteLikes() {
        storage.add(film);

        Film user1 = storage.remove(1L);

        assertEquals(1L, user1.getId());
        assertEquals(film, user1);
        assertEquals(0, storage.getAll().size());

        assertNull(getLikesField().get(1L));
    }

    @Test
    public void shouldThrowNotFoundWhenRemoveFilmThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> storage.remove(1L));
    }

    @Test
    public void shouldRemoveAllAndClearLikes() {
        storage.add(film);
        storage.clear();

        assertEquals(0, storage.getAll().size());
        assertEquals(0, getLikesField().size());
    }

    @Test
    public void shouldAddLike() {
        storage.add(film);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        assertEquals(2, film.getLikes());
        assertEquals(2, getLikesField().get(1L).size());
    }

    @Test
    public void shouldAddLikeOnlyOnce() {
        storage.add(film);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 1L);

        assertEquals(1, film.getLikes());
        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldThrowNotFoundWhenAddLikeToFilmThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> storage.addLike(1L, 1L));
    }

    @Test
    public void shouldRemoveLike() {
        storage.add(film);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        storage.removeLike(1L, 1L);

        assertEquals(1, film.getLikes());
        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldRemoveLikeOnlyOnce() {
        storage.add(film);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        storage.removeLike(1L, 1L);
        storage.removeLike(1L, 1L);

        assertEquals(1, film.getLikes());
        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldThrowNotFoundWhenRemoveLikeToFilmThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> storage.removeLike(1L, 1L));
    }

    @Test
    public void shouldReturnTopFilmsInOrder() {
        storage.add(film);
        storage.add(new Film());
        storage.add(new Film());

        storage.addLike(3L, 1L);
        storage.addLike(3L, 2L);
        storage.addLike(3L, 3L);

        storage.addLike(2L, 1L);
        storage.addLike(2L, 2L);

        storage.addLike(1L, 1L);


        List<Film> sortedFilms = List.copyOf(storage.getTopFilms(10L));
        assertEquals(3L, sortedFilms.get(0).getId());
        assertEquals(2L, sortedFilms.get(1).getId());
        assertEquals(1L, sortedFilms.get(2).getId());
    }

    @Test
    public void shouldReturnOnlyTwoTopFilmsInOrder() {
        storage.add(film);
        storage.add(new Film());
        storage.add(new Film());

        storage.addLike(3L, 1L);
        storage.addLike(3L, 2L);
        storage.addLike(3L, 3L);

        storage.addLike(2L, 1L);
        storage.addLike(2L, 2L);

        storage.addLike(1L, 1L);


        List<Film> sortedFilms = List.copyOf(storage.getTopFilms(2L));
        assertEquals(2, sortedFilms.size());
        assertEquals(3L, sortedFilms.get(0).getId());
        assertEquals(2L, sortedFilms.get(1).getId());
    }
}