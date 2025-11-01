package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        friendShipStorage.initializeFriendsSet(1L);
        friendShipStorage.initializeFriendsSet(2L);

        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(2L, 1L);

        assertTrue(getFriendShips().get(1L).contains(2L));
        assertTrue(getFriendShips().get(2L).contains(1L));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSenderIsNotFoundCreateFriendShip() {
        assertThrows(NullPointerException.class, () -> friendShipStorage.addFriend(1000L, 1L));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenReceiverIsNotFoundCreateFriendShip() {
        assertThrows(NullPointerException.class, () -> friendShipStorage.addFriend(1L, 1000L));
    }

    @Test
    public void shouldClearUserFriendsAndDeleteUser() {
        friendShipStorage.initializeFriendsSet(1L);
        friendShipStorage.clearFriendsSet(1L);
        assertNull(getFriendShips().get(1L));
    }

    @Test
    public void shouldBreakFriendShip() {
        friendShipStorage.initializeFriendsSet(1L);
        friendShipStorage.initializeFriendsSet(2L);

        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(2L, 1L);

        friendShipStorage.deleteFriend(1L, 2L);
        friendShipStorage.deleteFriend(2L, 1L);

        assertFalse(getFriendShips().get(1L).contains(2L));
        assertFalse(getFriendShips().get(2L).contains(1L));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSenderIsNotFoundBreakFriendShip() {
        assertThrows(NullPointerException.class, () -> friendShipStorage.deleteFriend(1000L, 1L));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenReceiverIsNotFoundBreakFriendShip() {
        assertThrows(NullPointerException.class, () -> friendShipStorage.deleteFriend(1L, 1000L));
    }

    @Test
    public void shouldReturnFriends() {
        friendShipStorage.initializeFriendsSet(1L);
        friendShipStorage.initializeFriendsSet(2L);

        friendShipStorage.addFriend(1L, 2L);
        friendShipStorage.addFriend(2L, 1L);

        assertTrue(friendShipStorage.getFriends(2L).contains(1L));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenUserIsNotFoundReturnFriends() {
        assertNull(friendShipStorage.getFriends(1000L));
    }
}