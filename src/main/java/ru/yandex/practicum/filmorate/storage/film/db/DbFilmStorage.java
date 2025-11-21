package ru.yandex.practicum.filmorate.storage.film.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository("DbFilmStorage")
public class DbFilmStorage extends BaseDao<Film> implements FilmStorage {

    private static final String SELECT_FIELDS = """
            SELECT f.*,
            COUNT(l.film_id) AS likes,
            m.mpa_id,
            m.name AS mpa_name,
            ARRAY_AGG(g.genre_id) AS genre_ids,
            ARRAY_AGG(g.name) AS genre_names
            """;

    private static final String FROM_JOIN_EVERYTHING = """
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id
            LEFT JOIN likes AS l ON f.film_id = l.film_id
            LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id
            LEFT JOIN genres AS g ON fg.genre_id = g.genre_id""";

    private static final String GROUP_BY = "GROUP BY f.film_id";
    private static final String EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM films WHERE film_id = ?)";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ?";

    private static final String SELECT_BY_ID_QUERY = String.format("%s %s WHERE f.film_id = ? %s",
            SELECT_FIELDS, FROM_JOIN_EVERYTHING, GROUP_BY);

    private static final String SELECT_ALL_QUERY = String.format("%s %s %s",
            SELECT_FIELDS, FROM_JOIN_EVERYTHING, GROUP_BY);

    private static final String UPDATE_QUERY = """
            UPDATE films
            SET name = :name, description = :description,
            release_date = :release_date, duration = :duration, mpa_id = :mpa_id
            WHERE film_id = :film_id""";


    private final SimpleJdbcInsert simpleInsert;

    public DbFilmStorage(JdbcTemplate jdbc, FilmMapper mapper) {
        super(jdbc, mapper);
        this.simpleInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
    }

    @Override
    public Film add(Film film) {
        Long id = simpleInsert.executeAndReturnKey(filmToMap(film)).longValue();
        film.setId(id);

        return film;
    }

    @Override
    public Film update(Film film) {
        Map<String, Object> map = filmToMap(film);
        map.put("film_id", film.getId());

        update(UPDATE_QUERY, map);

        return film;
    }

    @Override
    public Film remove(Long id) {
        Film film = get(id);
        remove(DELETE_QUERY, id);
        return film;
    }

    @Override
    public Film get(Long id) {
        return get(SELECT_BY_ID_QUERY, id);
    }

    @Override
    public Collection<Film> getAll() {
        return getAll(SELECT_ALL_QUERY);
    }

    @Override
    public boolean contains(Long id) {
        return jdbc.queryForObject(EXISTS_QUERY, Boolean.class, id);
    }

    @Override
    public Collection<Film> getTopFilms(Long count) {
        String sql = SELECT_ALL_QUERY + " ORDER BY likes DESC LIMIT ?";

        return jdbc.query(sql, mapper, count);
    }

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", film.getName());
        map.put("description", film.getDescription());
        map.put("release_date", film.getReleaseDate());
        map.put("duration", film.getDuration());

        if (film.getMpa() != null) {
            map.put("mpa_id", film.getMpa().getId());
        }

        return map;
    }
}
