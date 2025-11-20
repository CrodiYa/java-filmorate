package ru.yandex.practicum.filmorate.storage.film.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbFilmStorage.class, FilmMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFilmStorageTest {

    private final DbFilmStorage filmStorage;

    @Test
    public void shouldGetFilm() {
        Film film = filmStorage.get(1L);
        assertNotNull(film);
        assertEquals("name1", film.getName());
    }

    @Test
    public void shouldThrowWhenNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> filmStorage.get(1000L));
    }

    @Test
    public void shouldGetAll() {
        List<Film> list = (List<Film>) filmStorage.getAll();

        assertEquals(5, list.size());

        for (int i = 1; i <= list.size(); i++) {
            assertEquals("name" + i, list.get(i - 1).getName());
        }
    }

    @Test
    public void shouldReturnTrue() {
        boolean actual = filmStorage.contains(1L);
        assertTrue(actual);
    }

    @Test
    public void shouldReturnFalse() {
        boolean actual = filmStorage.contains(1000L);
        assertFalse(actual);
    }

    @Test
    public void shouldAddFilm() {
        Film film = new Film();
        film.setName("name6");
        film.setDescription("desc6");

        Film actual = filmStorage.add(film);

        assertEquals(6, actual.getId());

        assertTrue(filmStorage.contains(6L));
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = filmStorage.get(1L);
        film.setName("newName1");

        Film actual = filmStorage.update(film);

        assertEquals("newName1", actual.getName());
    }

    @Test
    public void shouldRemoveFilm() {
        filmStorage.remove(1L);
        assertFalse(filmStorage.contains(1L));
    }

    @Test
    public void shouldGetTopFilmsLimit() {

        Collection<Film> topFilms = filmStorage.getTopFilms(2L);

        assertNotNull(topFilms);
        assertEquals(2, topFilms.size());

        List<Film> resultList = new ArrayList<>(topFilms);
        assertEquals(3L, resultList.get(0).getId());
        assertEquals(2L, resultList.get(1).getId());
    }

    @Test
    public void shouldGetTopFilmsNoLimit() {

        Collection<Film> topFilms = filmStorage.getTopFilms(1000L);

        assertNotNull(topFilms);
        assertEquals(5, topFilms.size());

        List<Film> resultList = new ArrayList<>(topFilms);
        assertEquals(3L, resultList.get(0).getId());
        assertEquals(2L, resultList.get(1).getId());
        assertEquals(1L, resultList.get(2).getId());
    }
}
