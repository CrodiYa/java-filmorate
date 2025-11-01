package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserStorageTest {

    private UserStorage storage;
    private User user;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryUserStorage();
        user = new User();
        user.setId(1L);
    }

    private Map<Long, Map<Long, User>> getFriendShips() {

        try {
            Field field = storage.getClass().getDeclaredField("friendships");
            field.setAccessible(true);
            return (Map<Long, Map<Long, User>>) field.get(storage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Test
    public void shouldAddObject() {
        User user1 = storage.add(user);

        assertEquals(1L, user1.getId());
        assertEquals(user, user1);
    }

    @Test
    public void shouldUpdateObject() {
        storage.add(user);

        User user1 = storage.update(user);

        assertEquals(1L, user1.getId());
        assertEquals(user, user1);
    }

    @Test
    public void shouldRemoveObject() {
        storage.add(user);

        User user1 = storage.remove(1L);

        assertEquals(1L, user1.getId());
        assertEquals(user, user1);
        assertEquals(0, storage.getAll().size());
    }

    @Test
    public void shouldGetObject() {
        storage.add(user);
        User user1 = storage.get(1L);

        assertEquals(1L, user1.getId());
        assertEquals(user, user1);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGetObjectThatDoesNotExist() {
        assertNull(storage.get(1L));
    }

    @Test
    public void shouldReturnTrueIfContains() {
        storage.add(user);
        assertTrue(storage.contains(1L));
    }

    @Test
    public void shouldReturnFalseIfNotContains() {
        assertFalse(storage.contains(1L));
    }

    @Test
    public void shouldReturnSize() {
        storage.add(user);
        assertEquals(storage.getAll().size(), storage.size());
    }
}
