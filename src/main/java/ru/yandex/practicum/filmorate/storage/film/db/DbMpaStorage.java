package ru.yandex.practicum.filmorate.storage.film.db;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseDao;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mappers.MpaMapper;

import java.util.Collection;
import java.util.Optional;

@Repository("DbMpaStorage")
public class DbMpaStorage extends BaseDao<Mpa> implements MpaStorage {

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM mpa WHERE mpa_id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM mpa;";

    public DbMpaStorage(JdbcTemplate jdbc, MpaMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Mpa> getMpa(Long id) {
        try {
            Mpa mpa = get(SELECT_BY_ID_QUERY, id);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Mpa> getAll() {
        return getAll(SELECT_ALL_QUERY);
    }
}
