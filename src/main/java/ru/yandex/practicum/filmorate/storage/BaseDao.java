package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collection;
import java.util.Map;

public abstract class BaseDao<T> {
    protected final JdbcTemplate jdbc;
    protected final NamedParameterJdbcTemplate namedJdbc;
    protected final RowMapper<T> mapper;

    public BaseDao(JdbcTemplate jdbc, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc);
        this.mapper = mapper;
    }

    public void update(String sql, Map<String, Object> map) {
        namedJdbc.update(sql, map);
    }

    public void remove(String sql, Object... params) {
        jdbc.update(sql, params);
    }

    public T get(String sql, Object... params) {
        return jdbc.queryForObject(sql, mapper, params);
    }

    public Collection<T> getAll(String sql, Object... params) {
        return jdbc.query(sql, mapper, params);
    }
}
