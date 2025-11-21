package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.user.memory.InMemoryFriendShipStorage;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFriendShipStorageTest {

    private FriendShipStorage friendShipStorage;

    @BeforeEach
    public void setUp() {
        friendShipStorage = new InMemoryFriendShipStorage();
    }

    private Map<Long, Set<Long>> getFriendShips() {

        try {
            Field field = friendShipStorage.getClass().getDeclaredField("friendships");
            field.setAccessible(true);
            return (Map<Long, Set<Long>>) field.get(friendShipStorage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldCreateFriendShip() {

        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(2L, 1L);

        assertTrue(getFriendShips().get(1L).contains(2L));
        assertTrue(getFriendShips().get(2L).contains(1L));
    }

    @Test
    public void shouldClearUserFriendsAndDeleteUser() {
        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.deleteUserFromAllFriends(1L);
        assertNull(getFriendShips().get(1L));
    }

    @Test
    public void shouldBreakFriendShip() {

        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(2L, 1L);

        friendShipStorage.deleteFriend(1L, 2L);
        friendShipStorage.deleteFriend(2L, 1L);

        assertFalse(getFriendShips().get(1L).contains(2L));
        assertFalse(getFriendShips().get(2L).contains(1L));
    }

    @Test
    public void shouldReturnFriends() {

        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(2L, 1L);

        assertTrue(friendShipStorage.getFriends(2L).contains(1L));
    }

    @Test
    public void shouldGetCommonFriends() {
        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(1L, 3L);
        friendShipStorage.addFriend(2L, 3L);

        Set<Long> common = friendShipStorage.getCommonFriends(1L, 2L);

        assertTrue(common.contains(3L));
        assertFalse(common.contains(1L));
        assertFalse(common.contains(2L));
    }

    @Test
    public void shouldGetCommonFriendsReverseOrder() {
        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(1L, 3L);
        friendShipStorage.addFriend(2L, 3L);

        Set<Long> common = friendShipStorage.getCommonFriends(2L, 1L);

        assertTrue(common.contains(3L));
        assertFalse(common.contains(1L));
        assertFalse(common.contains(2L));
    }
}
