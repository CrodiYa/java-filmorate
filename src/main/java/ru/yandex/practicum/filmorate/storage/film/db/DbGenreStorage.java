package ru.yandex.practicum.filmorate.storage.film.db;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDao;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("DbGenreStorage")
public class DbGenreStorage extends BaseDao<Genre> implements GenreStorage {

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM genres;";

    public DbGenreStorage(JdbcTemplate jdbc, GenreMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Genre> getGenre(Long id) {
        try {
            Genre genre = get(SELECT_BY_ID_QUERY, id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Genre> getAll() {
        return getAll(SELECT_ALL_QUERY);
    }

    @Override
    public void addGenresToFilm(Film film) {
        String sql = "INSERT INTO films_genres(film_id, genre_id) VALUES(?,?)";
        Long filmId = film.getId();

        List<Object[]> batchArgs = film.getGenres()
                .stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        jdbc.batchUpdate(sql, batchArgs);
    }

    @Override
    public void updateFilmGenres(Film film) {
        String sql = "DELETE FROM films_genres WHERE film_id = ?";
        jdbc.update(sql, film.getId());
        addGenresToFilm(film);
    }
}
