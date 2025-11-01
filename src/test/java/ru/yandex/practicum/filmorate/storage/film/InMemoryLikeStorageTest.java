package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void shouldCreateLikesSet() {
        storage.initializeLikesSet(1L);
        assertNotNull(getLikesField().get(1L));
    }

    @Test
    public void shouldRemoveSet() {
        storage.initializeLikesSet(1L);
        storage.clearLikesSet(1L);
        assertNull(getLikesField().get(1L));
    }

    @Test
    public void shouldAddLike() {
        storage.initializeLikesSet(1L);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        assertEquals(2, getLikesField().get(1L).size());
    }

    @Test
    public void shouldAddLikeOnlyOnce() {
        storage.initializeLikesSet(1L);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 1L);

        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenAddLikeToFilmThatDoesNotExist() {
        assertThrows(NullPointerException.class, () -> storage.addLike(1L, 1L));
    }

    @Test
    public void shouldRemoveLike() {
        storage.initializeLikesSet(1L);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        storage.deleteLike(1L, 1L);

        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldRemoveLikeOnlyOnce() {
        storage.initializeLikesSet(1L);

        storage.addLike(1L, 1L);
        storage.addLike(1L, 2L);

        storage.deleteLike(1L, 1L);
        storage.deleteLike(1L, 1L);

        assertEquals(1, getLikesField().get(1L).size());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenRemoveLikeToFilmThatDoesNotExist() {
        assertThrows(NullPointerException.class, () -> storage.deleteLike(1L, 1L));
    }
}
