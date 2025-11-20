package ru.yandex.practicum.filmorate.storage.user.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbFriendShipStorage.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFriendShipStorageTest {

    private final DbFriendShipStorage storage;

    @Test
    public void shouldGetFriend() {
        Set<Long> friends = storage.getFriends(3L);
        assertTrue(friends.contains(1L));
        assertTrue(friends.contains(4L));
    }

    @Test
    public void shouldAddFriend() {
        storage.addFriend(1L, 4L);
        Set<Long> list = storage.getFriends(1L);
        assertTrue(list.contains(4L));
    }

    @Test
    public void shouldDeleteFriend() {
        storage.deleteFriend(3L, 1L);
        Set<Long> list = storage.getFriends(3L);
        assertFalse(list.contains(1L));
    }

    @Test
    public void shouldGetCommonFriends() {
        Set<Long> common = storage.getCommonFriends(2L, 3L);
        assertTrue(common.contains(1L));
    }

    @Test
    public void shouldGetCommonFriendsReverse() {
        Set<Long> common = storage.getCommonFriends(3L, 2L);
        assertTrue(common.contains(1L));
    }

    @Test
    public void shouldDoNothing() {
        assertDoesNotThrow(() -> storage.deleteUserFromAllFriends(1L));
    }
}
