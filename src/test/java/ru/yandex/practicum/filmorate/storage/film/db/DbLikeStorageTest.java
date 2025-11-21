package ru.yandex.practicum.filmorate.storage.film.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(DbLikeStorage.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbLikeStorageTest {

    private final DbLikeStorage storage;

    @Test
    public void shouldAddLikes() {
        Long likes = storage.addLike(1L, 5L);
        assertEquals(3L, likes);
    }

    @Test
    public void shouldDeleteLikes() {
        Long likes = storage.deleteLike(1L, 1L);
        assertEquals(1L, likes);
    }

    @Test
    public void shouldDoNothing() {
        assertDoesNotThrow(() -> storage.clearLikes(1L));
    }
}
