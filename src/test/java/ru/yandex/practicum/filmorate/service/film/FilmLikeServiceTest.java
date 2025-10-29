package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmLikeServiceTest {

    @Mock
    private FilmStorage filmStorage;

    @Mock
    private UserStorage userStorage;

    private FilmLikeService filmLikeService;

    @BeforeEach
    void setUp() {
        filmLikeService = new FilmLikeService(filmStorage, userStorage);
    }

    @Test
    void shouldAddLikeWhenUserExist() {
        // входящая информация
        Long filmId = 1L;
        Long userId = 1L;
        Film film = new Film();
        film.setId(filmId);

        // что мы должны получить
        Film filmWithLike = new Film();
        filmWithLike.setId(filmId);
        filmWithLike.setLikes(1L);

        // настраиваем поведение мока
        doNothing().when(userStorage).throwIfNotFound(userId);
        when(filmStorage.addLike(filmId, userId)).thenReturn(filmWithLike);

        Film result = filmLikeService.addLike(filmId, userId);

        // проверки, что метод вызвался
        verify(userStorage).throwIfNotFound(userId);
        verify(filmStorage).addLike(filmId, userId);

        // проверка, что значения равны
        assertEquals(1, result.getLikes());
    }

    @Test
    void shouldNotAddLikeAndThrowNotFoundExceptionWhenUserDoesNotExist() {
        Long userId = 1000L;
        Long filmId = 1L;
        Film film = new Film();
        film.setId(filmId);

        // говорю моку, что ему нужно НЕ НАЙТИ юзера
        doThrow(NotFoundException.class).when(userStorage).throwIfNotFound(userId);

        assertThrows(NotFoundException.class, () -> filmLikeService.addLike(filmId, userId));

        verify(userStorage).throwIfNotFound(userId);
        verify(filmStorage, never()).addLike(filmId, userId);
    }

    @Test
    void shouldRemoveLikeWhenUserExist() {
        // входящая информация
        Long filmId = 1L;
        Long userId = 1L;
        Film film = new Film();
        film.setId(filmId);
        film.setLikes(1L);

        // что мы должны получить
        Film filmWithLike = new Film();
        filmWithLike.setId(filmId);
        filmWithLike.setLikes(0L);

        // настраиваем поведение мока
        doNothing().when(userStorage).throwIfNotFound(userId);
        when(filmStorage.removeLike(filmId, userId)).thenReturn(filmWithLike);

        Film result = filmLikeService.removeLike(filmId, userId);

        // проверки, что метод вызвался
        verify(userStorage).throwIfNotFound(userId);
        verify(filmStorage).removeLike(filmId, userId);

        // проверка, что значения равны
        assertEquals(0, result.getLikes());
    }

    @Test
    void shouldNotRemoveLikeAndThrowNotFoundExceptionWhenUserDoesNotExist() {
        Long userId = 1000L;
        Long filmId = 1L;
        Film film = new Film();
        film.setId(filmId);
        film.setLikes(1L);

        // говорю моку, что ему нужно НЕ НАЙТИ юзера
        doThrow(NotFoundException.class).when(userStorage).throwIfNotFound(userId);

        assertThrows(NotFoundException.class, () -> filmLikeService.removeLike(filmId, userId));

        verify(userStorage).throwIfNotFound(userId);
        verify(filmStorage, never()).addLike(filmId, userId);
    }
}