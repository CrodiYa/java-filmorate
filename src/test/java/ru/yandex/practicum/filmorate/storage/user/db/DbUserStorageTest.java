package ru.yandex.practicum.filmorate.storage.user.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbUserStorage.class, UserMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbUserStorageTest {

    private final DbUserStorage userStorage;

    @Test
    public void shouldGetUser() {
        User user = userStorage.get(1L);
        assertNotNull(user);
        assertEquals("name1", user.getName());
    }

    @Test
    public void shouldThrowWhenNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> userStorage.get(1000L));
    }

    @Test
    public void shouldGetAll() {
        List<User> list = (List<User>) userStorage.getAll();

        assertEquals(5, list.size());

        for (int i = 1; i <= list.size(); i++) {
            assertEquals("name" + i, list.get(i - 1).getName());
        }
    }

    @Test
    public void shouldReturnTrue() {
        boolean actual = userStorage.contains(1L);
        assertTrue(actual);
    }

    @Test
    public void shouldReturnFalse() {
        boolean actual = userStorage.contains(1000L);
        assertFalse(actual);
    }

    @Test
    public void shouldAddUser() {
        User user = new User();
        user.setEmail("login6");
        user.setLogin("login6");

        User actual = userStorage.add(user);

        assertEquals(6, actual.getId());

        assertTrue(userStorage.contains(6L));
    }

    @Test
    public void shouldUpdateUser() {
        User user = userStorage.get(1L);
        user.setName("newName1");

        User actual = userStorage.update(user);

        assertEquals("newName1", actual.getName());
    }

    @Test
    public void shouldRemoveUser() {
        userStorage.remove(1L);
        assertFalse(userStorage.contains(1L));
    }

    @Test
    public void shouldGetAllFromCollection() {
        List<Long> ids = List.of(3L, 4L, 5L);
        List<User> users = userStorage.getAllFromCollection(ids);

        assertEquals(3, users.size());
        assertEquals("name3", users.get(0).getName());
        assertEquals("name4", users.get(1).getName());
        assertEquals("name5", users.get(2).getName());
    }

    @Test
    public void shouldIgnoreIfNotFound() {
        List<Long> ids = List.of(4000L, 5L, 6L);
        assertDoesNotThrow(() -> userStorage.getAllFromCollection(ids));
    }

    @Test
    public void shouldIgnoreIfEmpty() {
        List<Long> ids = Collections.emptyList();
        List<User> list = userStorage.getAllFromCollection(ids);
        assertTrue(list.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListIfNull() {
        List<User> list = userStorage.getAllFromCollection(null);
        assertTrue(list.isEmpty());
    }
}
