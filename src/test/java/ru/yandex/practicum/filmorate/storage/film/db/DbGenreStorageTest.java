package ru.yandex.practicum.filmorate.storage.film.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@Import({DbGenreStorage.class, GenreMapper.class, DbFilmStorage.class, FilmMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbGenreStorageTest {

    private final DbGenreStorage storage;
    private final DbFilmStorage filmStorage;

    @Test
    public void shouldGetGenre() {

        Optional<Genre> genreOptional = storage.getGenre(1L);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void shouldGetEmptyGenre() {
        Optional<Genre> genreOptional = storage.getGenre(1000L);
        assertThat(genreOptional).isEmpty();
    }

    @Test
    public void shouldGetAll() {
        List<Genre> list = (List<Genre>) storage.getAll();
        assertEquals(6, list.size());
        assertEquals("Боевик", list.getLast().getName());
    }

    @Test
    public void shouldAddGenresToFilm() {
        Film film = new Film();
        film.setId(1L);
        List<Genre> allGenres = (List<Genre>) storage.getAll();
        Set<Genre> genres = new HashSet<>();
        for (int i = 1; i <= 3; i++) {
            genres.add(allGenres.get(i - 1));
        }
        film.setGenres(genres);

        storage.addGenresToFilm(film);

        Set<Genre> actual = filmStorage.get(1L).getGenres();
        assertEquals(3, actual.size());
        assertEquals(genres, actual);
    }

    @Test
    public void shouldThrowIfGenreInvalidAddGenre() {
        Film film = new Film();
        film.setId(1L);
        Set<Genre> genres = Set.of(new Genre(1000L, "bla"));
        film.setGenres(genres);

        assertThrows(DataIntegrityViolationException.class, () -> storage.addGenresToFilm(film));
    }

    @Test
    public void shouldUpdateFilmGenres() {
        Film film = new Film();
        film.setId(1L);
        List<Genre> allGenres = (List<Genre>) storage.getAll();
        Set<Genre> genres = new HashSet<>();
        for (int i = 1; i <= 3; i++) {
            genres.add(allGenres.get(i - 1));
        }
        film.setGenres(genres);

        storage.updateFilmGenres(film);

        Set<Genre> actual = filmStorage.get(1L).getGenres();
        assertEquals(3, actual.size());
        assertEquals(genres, actual);
    }

    @Test
    public void shouldThrowIfGenreInvalidUpdateGenre() {
        Film film = new Film();
        film.setId(1L);
        Set<Genre> genres = Set.of(new Genre(1000L, "bla"));
        film.setGenres(genres);

        assertThrows(DataIntegrityViolationException.class, () -> storage.updateFilmGenres(film));
    }
}