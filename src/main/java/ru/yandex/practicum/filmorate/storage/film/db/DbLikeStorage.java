package ru.yandex.practicum.filmorate.storage.film.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;

@Repository("DbLikeStorage")
public class DbLikeStorage implements LikeStorage {

    private final JdbcTemplate jdbc;

    public DbLikeStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Long addLike(Long filmId, Long userId) {
        String insertSql = "INSERT INTO likes(film_id, user_id) VALUES(?, ?)";
        String countSql = "SELECT COUNT(*) FROM likes WHERE film_id = ?";
        jdbc.update(insertSql, filmId, userId);

        return jdbc.queryForObject(countSql, Long.class, filmId);
    }

    @Override
    public Long deleteLike(Long filmId, Long userId) {
        String deleteSql = "DELETE FROM likes WHERE user_id = ?";
        String countSql = "SELECT COUNT(*) FROM likes WHERE film_id = ?";

        jdbc.update(deleteSql, userId);
        return jdbc.queryForObject(countSql, Long.class, filmId);
    }
}
