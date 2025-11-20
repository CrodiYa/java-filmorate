package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.film.memory.InMemoryLikeStorage;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryLikeStorageTest {
    private LikeStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryLikeStorage();
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
    public void shouldAddLike() {
        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        assertEquals(2, getLikesField().get(1L).size());
    }

    @Test
    public void shouldAddLikeOnlyOnce() {

        storage.addLike(1L, 1L);
        storage.addLike(1L, 1L);

        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldRemoveLike() {

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        storage.deleteLike(1L, 1L);

        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldRemoveLikeOnlyOnce() {

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        storage.deleteLike(1L, 1L);
        storage.deleteLike(1L, 1L);

        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldClearLikes() {
        storage.addLike(1L, 2L);
        storage.clearLikes(1L);
        assertFalse(getLikesField().containsKey(1L));
    }
}
