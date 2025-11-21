package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @Mock
    private FilmStorage filmStorage;

    @Mock
    private LikeStorage likeStorage;

    @Mock
    private UserStorage userStorage;

    @Mock
    private GenreStorage genreStorage;

    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        filmService = new FilmService(filmStorage, userStorage, likeStorage, genreStorage);
    }

    @Test
    public void shouldGetFilmSuccessfully() {
        Long filmId = 1L;
        Film expectedFilm = new Film(filmId, "Film Name", "Description", LocalDate.now(), 120);

        when(filmStorage.contains(filmId)).thenReturn(true);
        when(filmStorage.get(filmId)).thenReturn(expectedFilm);

        Film actualFilm = filmService.getFilm(filmId);

        assertNotNull(actualFilm);
        assertEquals(expectedFilm, actualFilm);
        verify(filmStorage).contains(filmId);
        verify(filmStorage).get(filmId);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenFilmNotExists() {
        Long filmId = 999L;

        when(filmStorage.contains(filmId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> filmService.getFilm(filmId));
        verify(filmStorage, never()).get(filmId);
    }

    @Test
    public void shouldGetAllFilms() {
        Film film1 = new Film(1L, "Film 1", "Desc 1", LocalDate.now(), 120);
        Film film2 = new Film(2L, "Film 2", "Desc 2", LocalDate.now(), 150);
        Collection<Film> expectedFilms = List.of(film1, film2);

        when(filmStorage.getAll()).thenReturn(expectedFilms);

        Collection<Film> actualFilms = filmService.getAllFilms();

        assertNotNull(actualFilms);
        assertEquals(2, actualFilms.size());
        assertEquals(expectedFilms, actualFilms);
        verify(filmStorage).getAll();
    }

    @Test
    public void shouldAddFilmSuccessfully() {
        Film inputFilm = new Film(null, "New Film", "Description", LocalDate.now(), 120);
        Film savedFilm = new Film(1L, "New Film", "Description", LocalDate.now(), 120);

        when(filmStorage.add(inputFilm)).thenReturn(savedFilm);

        Film result = filmService.addFilm(inputFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(filmStorage).add(inputFilm);
    }

    @Test
    public void shouldAddFilmWithGenres() {
        Film inputFilm = new Film(null, "New Film", "Description", LocalDate.now(), 120);
        inputFilm.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));

        Film savedFilm = new Film(1L, "New Film", "Description", LocalDate.now(), 120);
        savedFilm.setGenres(inputFilm.getGenres());

        when(filmStorage.add(inputFilm)).thenReturn(savedFilm);

        Film result = filmService.addFilm(inputFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(filmStorage).add(inputFilm);
        verify(genreStorage).addGenresToFilm(savedFilm);
    }

    @Test
    public void shouldAddFilmWithoutGenres() {
        Film inputFilm = new Film(null, "New Film", "Description", LocalDate.now(), 120);
        inputFilm.setGenres(null);

        Film savedFilm = new Film(1L, "New Film", "Description", LocalDate.now(), 120);

        when(filmStorage.add(inputFilm)).thenReturn(savedFilm);

        Film result = filmService.addFilm(inputFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(filmStorage).add(inputFilm);
        verify(genreStorage, never()).addGenresToFilm(any());
    }

    @Test
    public void shouldAddFilmWithEmptyGenres() {
        Film inputFilm = new Film(null, "New Film", "Description", LocalDate.now(), 120);
        inputFilm.setGenres(Collections.emptySet());

        Film savedFilm = new Film(1L, "New Film", "Description", LocalDate.now(), 120);

        when(filmStorage.add(inputFilm)).thenReturn(savedFilm);

        Film result = filmService.addFilm(inputFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(filmStorage).add(inputFilm);
        verify(genreStorage, never()).addGenresToFilm(any());
    }

    @Test
    public void shouldUpdateFilmSuccessfully() {
        Film filmToUpdate = new Film(1L, "Updated Film", "Updated Desc", LocalDate.now(), 130);

        when(filmStorage.contains(1L)).thenReturn(true);
        when(filmStorage.update(filmToUpdate)).thenReturn(filmToUpdate);

        Film result = filmService.updateFilm(filmToUpdate);

        assertNotNull(result);
        assertEquals("Updated Film", result.getName());
        verify(filmStorage).update(filmToUpdate);
    }

    @Test
    public void shouldThrowValidationExceptionWhenUpdateFilmWithNullId() {
        Film filmWithoutId = new Film(null, "Film", "Desc", LocalDate.now(), 120);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmService.updateFilm(filmWithoutId));

        assertEquals("Id должен быть указан", exception.getMessage());
        verify(filmStorage, never()).update(any());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateNonExistentFilm() {
        Film filmToUpdate = new Film(999L, "Film", "Desc", LocalDate.now(), 120);

        when(filmStorage.contains(999L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> filmService.updateFilm(filmToUpdate));
        verify(filmStorage, never()).update(any());
    }

    @Test
    public void shouldUpdateFilmWithGenres() {
        Film inputFilm = new Film(1L, "Updated Film", "Updated Description", LocalDate.now(), 150);
        inputFilm.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(3L, "Мультфильм")));

        Film updatedFilm = new Film(1L, "Updated Film", "Updated Description", LocalDate.now(), 150);
        updatedFilm.setGenres(inputFilm.getGenres());

        when(filmStorage.contains(1L)).thenReturn(true);
        when(filmStorage.update(inputFilm)).thenReturn(updatedFilm);

        Film result = filmService.updateFilm(inputFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(filmStorage).update(inputFilm);
        verify(genreStorage).updateFilmGenres(updatedFilm);
    }

    @Test
    public void shouldUpdateFilmWithoutGenres() {
        Film inputFilm = new Film(1L, "Updated Film", "Updated Description", LocalDate.now(), 150);
        inputFilm.setGenres(null);

        Film updatedFilm = new Film(1L, "Updated Film", "Updated Description", LocalDate.now(), 150);

        when(filmStorage.contains(1L)).thenReturn(true);
        when(filmStorage.update(inputFilm)).thenReturn(updatedFilm);

        Film result = filmService.updateFilm(inputFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(filmStorage).update(inputFilm);
        verify(genreStorage, never()).updateFilmGenres(any());
    }

    @Test
    public void shouldDeleteFilmSuccessfully() {
        Long filmId = 1L;

        when(filmStorage.contains(filmId)).thenReturn(true);

        filmService.deleteFilm(filmId);

        verify(filmStorage).remove(filmId);
        verify(likeStorage).clearLikes(filmId);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenDeleteNonExistentFilm() {
        Long filmId = 999L;

        when(filmStorage.contains(filmId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> filmService.deleteFilm(filmId));
        verify(filmStorage, never()).remove(filmId);
        verify(likeStorage, never()).clearLikes(filmId);
    }

    @Test
    public void shouldAddLikeSuccessfully() {
        Long filmId = 1L;
        Long userId = 1L;
        Film film = new Film(filmId, "Film", "Desc", LocalDate.now(), 120);

        when(filmStorage.contains(filmId)).thenReturn(true);
        when(userStorage.contains(userId)).thenReturn(true);
        when(likeStorage.addLike(filmId, userId)).thenReturn(5L);
        when(filmStorage.get(filmId)).thenReturn(film);

        Film result = filmService.addLike(filmId, userId);

        assertNotNull(result);
        assertEquals(5L, result.getLikes());
        verify(likeStorage).addLike(filmId, userId);
        verify(filmStorage).get(filmId);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenAddLikeToNonExistentFilm() {
        Long filmId = 999L;
        Long userId = 1L;

        when(filmStorage.contains(filmId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> filmService.addLike(filmId, userId));
        verify(likeStorage, never()).addLike(anyLong(), anyLong());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenAddLikeFromNonExistentUser() {
        Long filmId = 1L;
        Long userId = 999L;

        when(filmStorage.contains(filmId)).thenReturn(true);
        when(userStorage.contains(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> filmService.addLike(filmId, userId));
        verify(likeStorage, never()).addLike(anyLong(), anyLong());
    }

    @Test
    public void shouldRemoveLikeSuccessfully() {
        Long filmId = 1L;
        Long userId = 1L;
        Film film = new Film(filmId, "Film", "Desc", LocalDate.now(), 120);

        when(filmStorage.contains(filmId)).thenReturn(true);
        when(userStorage.contains(userId)).thenReturn(true);
        when(likeStorage.deleteLike(filmId, userId)).thenReturn(3L);
        when(filmStorage.get(filmId)).thenReturn(film);

        Film result = filmService.removeLike(filmId, userId);

        assertNotNull(result);
        assertEquals(3L, result.getLikes());
        verify(likeStorage).deleteLike(filmId, userId);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenRemoveLikeFromNonExistentFilm() {
        Long filmId = 999L;
        Long userId = 1L;

        when(filmStorage.contains(filmId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> filmService.removeLike(filmId, userId));
        verify(likeStorage, never()).deleteLike(anyLong(), anyLong());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenRemoveLikeFromNonExistentUser() {
        Long filmId = 1L;
        Long userId = 999L;

        when(filmStorage.contains(filmId)).thenReturn(true);
        when(userStorage.contains(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> filmService.removeLike(filmId, userId));
        verify(likeStorage, never()).deleteLike(anyLong(), anyLong());
    }

    @Test
    public void shouldGetTopFilmsOrderedByLikes() {
        Film film1 = new Film(1L, "Film 1", "Desc 1", LocalDate.now(), 120);
        film1.setLikes(10L);
        Film film2 = new Film(2L, "Film 2", "Desc 2", LocalDate.now(), 150);
        film2.setLikes(25L);
        Film film3 = new Film(3L, "Film 3", "Desc 3", LocalDate.now(), 130);
        film3.setLikes(5L);

        when(filmStorage.getTopFilms(2L)).thenReturn(List.of(film2, film1));

        Collection<Film> topFilms = filmService.getTopFilms(2L);

        assertNotNull(topFilms);
        assertEquals(2, topFilms.size());

        List<Film> resultList = new ArrayList<>(topFilms);
        assertEquals(25L, resultList.get(0).getLikes()); // film2
        assertEquals(10L, resultList.get(1).getLikes()); // film1
        verify(filmStorage).getTopFilms(2L);
    }

    @Test
    public void shouldReturnEmptyListWhenNoFilms() {
        Collection<Film> topFilms = filmService.getTopFilms(10L);

        assertNotNull(topFilms);
        assertTrue(topFilms.isEmpty());
    }

    @Test
    public void shouldReturnAllFilmsWhenCountIsGreaterThanTotal() {
        List<Film> allFilms = List.of(
                new Film(1L, "Film 1", "Desc 1", LocalDate.now(), 120),
                new Film(2L, "Film 2", "Desc 2", LocalDate.now(), 150),
                new Film(3L, "Film 3", "Desc 3", LocalDate.now(), 130)
        );

        when(filmStorage.getTopFilms(10L)).thenReturn(allFilms);

        Collection<Film> topFilms = filmService.getTopFilms(10L);

        assertNotNull(topFilms);
        assertEquals(3, topFilms.size());
        verify(filmStorage).getTopFilms(10L);
    }
}