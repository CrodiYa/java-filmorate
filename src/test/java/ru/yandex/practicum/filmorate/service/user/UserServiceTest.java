package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendShipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserStorage userStorage;

    @Mock
    private FriendShipStorage friendShipStorage;

    private UserServiceInterface userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userStorage, friendShipStorage);
    }

    @Test
    public void shouldGetUser() {
        User user = new User(1L, "", "login", null, LocalDate.now());
        when(userStorage.contains(1L)).thenReturn(true);
        when(userStorage.get(1L)).thenReturn(user);
        User result = userService.getUser(1L);

        assertEquals(user, result);

        verify(userStorage).contains(any());
        verify(userStorage).get(any());
    }

    @Test
    public void shouldThrowNotFoundWhenGetUser() {
        when(userStorage.contains(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.getUser(1L));

        verify(userStorage).contains(any());
        verify(userStorage, never()).get(any());
    }

    @Test
    public void shouldGetAllUser() {
        User user1 = new User(1L, "", "login", null, LocalDate.now());
        User user2 = new User(2L, "", "login", null, LocalDate.now());
        List<User> list = List.of(user1, user2);

        when(userStorage.getAll()).thenReturn(list);

        assertEquals(2, userService.getAllUsers().size());

        verify(userStorage).getAll();
    }

    @Test
    public void shouldAddUser() {
        User user1 = new User(null, "email@mail.com", "login", "name", LocalDate.now());
        User returnUser = new User(1L, "email@mail.com", "login", "name", LocalDate.now());

        when(userStorage.add(any(User.class))).thenReturn(returnUser);
        doNothing().when(friendShipStorage).initializeFriendsSet(1L);

        User result = userService.addUser(user1);

        assertEquals(returnUser, result);
        verify(userStorage).add(any(User.class));
        verify(friendShipStorage).initializeFriendsSet(1L);
    }

    @Test
    public void shouldSetNameAsLoginWhenNameIsNull() {
        // входящая информация
        User user = new User(1L, "", "login", null, LocalDate.now());

        // настраиваем поведение мока
        when(userStorage.add(user)).thenReturn(user);

        User result = userService.addUser(user);

        // проверки, что метод вызвался
        verify(userStorage).add(user);

        // проверка, что значения равны
        assertEquals("login", result.getName());
    }

    @Test
    public void shouldSetNameAsLoginWhenNameIsEmpty() {
        // входящая информация
        User user = new User(1L, "", "login", "", LocalDate.now());

        // настраиваем поведение мока
        when(userStorage.add(user)).thenReturn(user);

        User result = userService.addUser(user);

        // проверки, что метод вызвался
        verify(userStorage).add(user);

        // проверка, что значения равны
        assertEquals("login", result.getName());
    }

    @Test
    public void shouldSetNameAsLoginWhenNameIsBlank() {
        // входящая информация
        User user = new User(1L, "", "login", "   ", LocalDate.now());

        // настраиваем поведение мока
        when(userStorage.add(user)).thenReturn(user);

        User result = userService.addUser(user);

        // проверки, что метод вызвался
        verify(userStorage).add(user);
        // проверка, что значения равны
        assertEquals("login", result.getName());
    }

    @Test
    public void shouldDoNothingWhenNameIsPresent() {
        // входящая информация
        User user = new User(1L, "", "login", "name", LocalDate.now());

        // настраиваем поведение мока
        when(userStorage.add(user)).thenReturn(user);

        User result = userService.addUser(user);

        // проверки, что метод вызвался
        verify(userStorage).add(user);

        // проверка, что значения равны
        assertEquals("name", result.getName());
    }

    @Test
    public void shouldUpdateUser() {
        User user1 = new User(1L, "email@mail.com", "login", "name", LocalDate.now());
        User returnUser = new User(1L, "email@mail.com", "login", "NAME", LocalDate.now());

        when(userStorage.contains(any())).thenReturn(true);
        when(userStorage.update(any(User.class))).thenReturn(returnUser);

        User result = userService.updateUser(user1);

        assertEquals(returnUser, result);
        verify(userStorage).update(any(User.class));
    }

    @Test
    public void shouldThrowValidationExceptionWhenUpdateUserWhenIdNull() {
        User user1 = new User(null, "email@mail.com", "login", "name", LocalDate.now());

        assertThrows(ValidationException.class, () -> userService.updateUser(user1));
        verify(userStorage, never()).update(any(User.class));
    }

    @Test
    public void shouldThrowNotFoundWhenUpdateUserWhenIdNotFound() {
        User user1 = new User(1L, "email@mail.com", "login", "name", LocalDate.now());

        when(userStorage.contains(any())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.updateUser(user1));
        verify(userStorage, never()).update(any(User.class));
    }

    @Test
    public void shouldDeleteUser() {
        Long userId = 1L;
        User user = new User(userId, "email@mail.com", "login", "name", LocalDate.now());

        when(userStorage.contains(userId)).thenReturn(true);
        when(friendShipStorage.getFriends(userId)).thenReturn(new HashSet<>(Set.of(2L, 3L)));
        when(friendShipStorage.getFriends(2L)).thenReturn(new HashSet<>(Set.of(1L, 4L)));
        when(friendShipStorage.getFriends(3L)).thenReturn(new HashSet<>(Set.of(1L, 5L)));

        userService.deleteUser(userId);

        verify(userStorage).remove(userId);
        verify(friendShipStorage).clearFriendsSet(userId);
        verify(friendShipStorage).getFriends(2L);
        verify(friendShipStorage).getFriends(3L);
    }

    @Test
    public void shouldThrowNotFoundWhenDeleteUserWhenIdNotFound() {
        when(userStorage.contains(1000L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1000L));
        verify(userStorage, never()).remove(1000L);
        verify(friendShipStorage, never()).clearFriendsSet(1000L);
    }

    @Test
    public void shouldRemoveUserFromFriendsFriendsLists() {
        Long userId = 1L;
        Set<Long> friends = Set.of(2L, 3L);

        Set<Long> friend2Friends = new HashSet<>(Set.of(1L, 4L, 5L));
        Set<Long> friend3Friends = new HashSet<>(Set.of(1L, 6L));

        when(userStorage.contains(userId)).thenReturn(true);
        when(friendShipStorage.getFriends(userId)).thenReturn(friends);
        when(friendShipStorage.getFriends(2L)).thenReturn(friend2Friends);
        when(friendShipStorage.getFriends(3L)).thenReturn(friend3Friends);

        userService.deleteUser(userId);

        assertFalse(friend2Friends.contains(userId));
        assertFalse(friend3Friends.contains(userId));
        verify(userStorage).remove(userId);
    }

    @Test
    public void shouldAddFriendSuccessfully() {
        Long senderId = 1L;
        Long receiverId = 2L;

        when(userStorage.contains(senderId)).thenReturn(true);
        when(userStorage.contains(receiverId)).thenReturn(true);

        userService.addFriend(senderId, receiverId);

        verify(friendShipStorage).addFriend(senderId, receiverId);
        verify(friendShipStorage).addFriend(receiverId, senderId);
    }

    @Test
    public void shouldThrowValidationExceptionWhenAddingSelfAsFriend() {
        Long userId = 1L;

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.addFriend(userId, userId));

        assertEquals("Сам себя не добавишь - никто не добавит", exception.getMessage());
        verify(friendShipStorage, never()).addFriend(anyLong(), anyLong());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenSenderNotFound() {
        Long senderId = 999L;
        Long receiverId = 2L;

        when(userStorage.contains(senderId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.addFriend(senderId, receiverId));
        verify(friendShipStorage, never()).addFriend(anyLong(), anyLong());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenReceiverNotFound() {
        Long senderId = 1L;
        Long receiverId = 999L;

        when(userStorage.contains(senderId)).thenReturn(true);
        when(userStorage.contains(receiverId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.addFriend(senderId, receiverId));
        verify(friendShipStorage, never()).addFriend(anyLong(), anyLong());
    }

    @Test
    public void shouldDeleteFriendSuccessfully() {
        Long senderId = 1L;
        Long receiverId = 2L;

        when(userStorage.contains(senderId)).thenReturn(true);
        when(userStorage.contains(receiverId)).thenReturn(true);

        userService.deleteFriend(senderId, receiverId);

        verify(friendShipStorage).deleteFriend(senderId, receiverId);
        verify(friendShipStorage).deleteFriend(receiverId, senderId);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenDeletingFriendAndSenderNotFound() {
        Long senderId = 999L;
        Long receiverId = 2L;

        when(userStorage.contains(senderId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteFriend(senderId, receiverId));
        verify(friendShipStorage, never()).deleteFriend(anyLong(), anyLong());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenDeletingFriendAndReceiverNotFound() {
        Long senderId = 1L;
        Long receiverId = 999L;

        when(userStorage.contains(senderId)).thenReturn(true);
        when(userStorage.contains(receiverId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteFriend(senderId, receiverId));
        verify(friendShipStorage, never()).deleteFriend(anyLong(), anyLong());
    }

    @Test
    public void shouldGetFriendsSuccessfully() {
        Long userId = 1L;
        Set<Long> friendIds = Set.of(2L, 3L);
        User friend1 = new User(2L, "friend1@mail.com", "friend1", "Friend One", LocalDate.now());
        User friend2 = new User(3L, "friend2@mail.com", "friend2", "Friend Two", LocalDate.now());

        when(userStorage.contains(userId)).thenReturn(true);
        when(friendShipStorage.getFriends(userId)).thenReturn(friendIds);
        when(userStorage.get(2L)).thenReturn(friend1);
        when(userStorage.get(3L)).thenReturn(friend2);

        Collection<User> friends = userService.getFriends(userId);

        assertNotNull(friends);
        assertEquals(2, friends.size());
        assertTrue(friends.contains(friend1));
        assertTrue(friends.contains(friend2));
        verify(friendShipStorage).getFriends(userId);
        verify(userStorage).get(2L);
        verify(userStorage).get(3L);
    }

    @Test
    public void shouldReturnEmptyFriendsList() {
        Long userId = 1L;

        when(userStorage.contains(userId)).thenReturn(true);
        when(friendShipStorage.getFriends(userId)).thenReturn(Set.of());

        Collection<User> friends = userService.getFriends(userId);

        assertNotNull(friends);
        assertTrue(friends.isEmpty());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGettingFriendsOfNonExistentUser() {
        Long userId = 999L;

        when(userStorage.contains(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.getFriends(userId));
        verify(friendShipStorage, never()).getFriends(anyLong());
    }

    @Test
    public void shouldGetCommonFriendsSuccessfully() {
        Long user1Id = 1L;
        Long user2Id = 2L;
        Set<Long> user1Friends = Set.of(3L, 4L, 5L);
        Set<Long> user2Friends = Set.of(4L, 5L, 6L);

        User commonFriend1 = new User(4L, "common1@mail.com", "common1", "Common One", LocalDate.now());
        User commonFriend2 = new User(5L, "common2@mail.com", "common2", "Common Two", LocalDate.now());

        when(userStorage.contains(user1Id)).thenReturn(true);
        when(userStorage.contains(user2Id)).thenReturn(true);
        when(friendShipStorage.getFriends(user1Id)).thenReturn(user1Friends);
        when(friendShipStorage.getFriends(user2Id)).thenReturn(user2Friends);
        when(userStorage.get(4L)).thenReturn(commonFriend1);
        when(userStorage.get(5L)).thenReturn(commonFriend2);

        Collection<User> commonFriends = userService.getCommonFriends(user1Id, user2Id);

        assertNotNull(commonFriends);
        assertEquals(2, commonFriends.size());
        assertTrue(commonFriends.contains(commonFriend1));
        assertTrue(commonFriends.contains(commonFriend2));
    }

    @Test
    public void shouldReturnEmptyCommonFriendsWhenNoCommonFriends() {
        Long user1Id = 1L;
        Long user2Id = 2L;
        Set<Long> user1Friends = Set.of(3L, 4L);
        Set<Long> user2Friends = Set.of(5L, 6L);

        when(userStorage.contains(user1Id)).thenReturn(true);
        when(userStorage.contains(user2Id)).thenReturn(true);
        when(friendShipStorage.getFriends(user1Id)).thenReturn(user1Friends);
        when(friendShipStorage.getFriends(user2Id)).thenReturn(user2Friends);

        Collection<User> commonFriends = userService.getCommonFriends(user1Id, user2Id);

        assertNotNull(commonFriends);
        assertTrue(commonFriends.isEmpty());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenFirstUserNotFoundInCommonFriends() {
        Long user1Id = 999L;
        Long user2Id = 2L;

        when(userStorage.contains(user1Id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.getCommonFriends(user1Id, user2Id));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenSecondUserNotFoundInCommonFriends() {
        Long user1Id = 1L;
        Long user2Id = 999L;

        when(userStorage.contains(user1Id)).thenReturn(true);
        when(userStorage.contains(user2Id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.getCommonFriends(user1Id, user2Id));
    }
}
