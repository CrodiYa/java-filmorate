package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendShipServiceTest {

    @Mock
    private UserStorage userStorage;

    private FriendShipService friendShipService;

    @BeforeEach
    void setUp() {
        friendShipService = new FriendShipService(userStorage);
    }

    public Collection<User> addFriend(Long senderId, Long receiverId) {
        if (Objects.equals(receiverId, senderId)) {
            throw new ValidationException("Сам себя не добавишь - никто не добавит");
        }

        return userStorage.addFriend(senderId, receiverId);
    }

    @Test
    void shouldThrowValidationExceptionWhenIdsAreEqual() {
        assertThrows(ValidationException.class, () -> friendShipService.addFriend(1L, 1L));
        verify(userStorage, never()).addFriend(any(), any());
    }

    @Test
    void shouldReturnCollectionWhenIdsAreNotEqual() {
        Long senderId = 1L;
        Long receiverId = 2L;
        List<User> expectedFriends = List.of(
                new User(2L, "email", "friend", "Friend", LocalDate.now())
        );

        when(userStorage.addFriend(senderId, receiverId)).thenReturn(expectedFriends);

        Collection<User> result = friendShipService.addFriend(senderId, receiverId);

        assertThat(result, is(expectedFriends));
        verify(userStorage).addFriend(senderId, receiverId);
    }
}