package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest {

    private UserStorage storage;
    private User user;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryUserStorage();
        user = new User();
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
    public void shouldAddUserAndCreateFriendsMap() {
        User user1 = storage.add(user);

        assertEquals(1L, user1.getId());
        assertEquals(user, user1);
        assertNotNull(getFriendShips().get(1L));
    }

    @Test
    public void shouldRemoveUserAndDeleteFriendShips() {
        storage.add(user);
        storage.add(new User());
        storage.add(new User());

        storage.addFriend(1L, 2L);
        storage.addFriend(1L, 3L);

        User user1 = storage.remove(1L);


        assertEquals(1L, user1.getId());
        assertEquals(user, user1);
        assertEquals(2, storage.getAll().size());

        assertFalse(getFriendShips().containsKey(1L));
        assertFalse(getFriendShips().get(2L).containsKey(1L));
        assertFalse(getFriendShips().get(3L).containsKey(1L));
    }

    @Test
    public void shouldThrowNotFoundWhenRemoveUserThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> storage.remove(1L));
    }

    @Test
    public void shouldRemoveAllAndClearFriends() {
        storage.add(user);
        storage.clear();

        assertEquals(0, storage.getAll().size());
        assertEquals(0, getFriendShips().size());
    }

    @Test
    public void shouldCreateFriendShip() {
        storage.add(user);
        storage.add(new User());

        storage.addFriend(1L, 2L);

        assertTrue(getFriendShips().get(1L).containsKey(2L));
        assertTrue(getFriendShips().get(2L).containsKey(1L));
    }

    @Test
    public void shouldThrowNotFoundWhenSenderIsNotFoundCreateFriendShip() {
        storage.add(user);

        assertThrows(NotFoundException.class, () -> storage.addFriend(1000L, 1L));
    }

    @Test
    public void shouldThrowNotFoundWhenReceiverIsNotFoundCreateFriendShip() {
        storage.add(user);

        assertThrows(NotFoundException.class, () -> storage.addFriend(1L, 1000L));
    }

    @Test
    public void shouldBreakFriendShip() {
        storage.add(user);
        storage.add(new User());

        storage.addFriend(1L, 2L);
        storage.deleteFriend(1L, 2L);

        assertFalse(getFriendShips().get(1L).containsKey(2L));
        assertFalse(getFriendShips().get(2L).containsKey(1L));
    }

    @Test
    public void shouldThrowNotFoundWhenSenderIsNotFoundBreakFriendShip() {
        storage.add(user);

        assertThrows(NotFoundException.class, () -> storage.deleteFriend(1000L, 1L));
    }

    @Test
    public void shouldThrowNotFoundWhenReceiverIsNotFoundBreakFriendShip() {
        storage.add(user);

        assertThrows(NotFoundException.class, () -> storage.deleteFriend(1L, 1000L));
    }

    @Test
    public void shouldReturnFriends() {
        storage.add(user);
        storage.add(new User());

        storage.addFriend(1L, 2L);

        assertTrue(storage.getFriends(2L).contains(user));
    }

    @Test
    public void shouldThrowNotFoundWhenUserIsNotFoundReturnFriends() {
        assertThrows(NotFoundException.class, () -> storage.getFriends(1000L));
    }

    @Test
    public void shouldReturnCommonFriends() {
        storage.add(user);
        User user2 = storage.add(new User());
        User user3 = storage.add(new User());

        storage.addFriend(1L, 2L);
        storage.addFriend(2L, 3L);

        assertTrue(storage.getCommonFriends(1L, 3L).contains(user2));
        assertFalse(storage.getCommonFriends(1L, 3L).contains(user));
        assertFalse(storage.getCommonFriends(1L, 3L).contains(user3));

        assertTrue(storage.getCommonFriends(1L, 2L).isEmpty());
        assertTrue(storage.getCommonFriends(2L, 3L).isEmpty());
    }

    @Test
    public void shouldThrowNotFoundWhenSenderIsNotFoundReturnCommonFriends() {
        assertThrows(NotFoundException.class, () -> storage.getCommonFriends(1000L, 1L));
    }

    @Test
    public void shouldThrowNotFoundWhenReceiverIsNotFoundReturnCommonFriends() {
        storage.add(user);

        assertThrows(NotFoundException.class, () -> storage.getCommonFriends(1L, 1000L));
    }

}