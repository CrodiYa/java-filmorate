package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserStorage userStorage;

    @Mock
    private FriendShipService friendShipService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userStorage, friendShipService);
    }

    @Test
    void shouldSetNameAsLoginWhenNameIsNull() {
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
    void shouldSetNameAsLoginWhenNameIsEmpty() {
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
    void shouldSetNameAsLoginWhenNameIsBlank() {
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
    void shouldDoNothingWhenNameIsPresent() {
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
}