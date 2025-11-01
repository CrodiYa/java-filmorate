package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

/**
 * Main service for film operations and business logic.
 * <p>
 * Provides comprehensive film management functionality including CRUD operations, likes management,
 * and popular films retrieval. Delegates likes-specific operations to FilmLikeService.
 * </p>
 *
 * @see Film
 * @see FilmStorage
 */
@Service
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    /**
     * Constructor for dependency injection
     */
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    /**
     * Retrieves specific film from the storage.
     *
     * @param id Film`s id to get.
     * @return Film.
     * @throws NotFoundException if film not found
     */
    @Override
    public Film getFilm(Long id) {
        throwIfNotFound(id);
        return filmStorage.get(id);
    }

    /**
     * Retrieves all filmsStorage from the storage.
     *
     * @return Collection of all filmsStorage.
     */
    @Override
    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    /**
     * Adds film to the storage.
     *
     * @param film Film to add.
     * @return created Film.
     */
    @Override
    public Film addFilm(Film film) {
        Film returnFilm = filmStorage.add(film);
        likeStorage.initializeLikesSet(returnFilm.getId());
        return returnFilm;
    }

    /**
     * Updates film in the storage.
     *
     * @param film Film to update.
     * @return updated Film.
     * @throws NotFoundException if film not found
     */
    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        throwIfNotFound(film.getId());

        return filmStorage.update(film);
    }

    /**
     * Deletes film from the storage.
     *
     * @param id Film`s id to delete.
     * @throws NotFoundException if film not found
     */
    @Override
    public void deleteFilm(Long id) {
        throwIfNotFound(id);
        filmStorage.remove(id);
        likeStorage.clearLikesSet(id);
    }

    /**
     * Adds like to the film.
     *
     * @param filmId Film`s id to add like.
     * @param userId User`s id to set like.
     * @return Film where likes where added.
     * @throws NotFoundException if film or user not found
     */
    @Override
    public Film addLike(Long filmId, Long userId) {
        throwIfNotFound(filmId);
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Long newLikes = likeStorage.addLike(filmId, userId);
        filmStorage.get(filmId).setLikes(newLikes);

        return filmStorage.get(filmId);
    }

    /**
     * Deletes like from the film.
     *
     * @param filmId Film`s id to delete like.
     * @param userId User`s id to delete like.
     * @return Film where likes where deleted.
     * @throws NotFoundException if film or user not found
     */
    @Override
    public Film removeLike(Long filmId, Long userId) {
        throwIfNotFound(filmId);
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Long newLikes = likeStorage.deleteLike(filmId, userId);
        filmStorage.get(filmId).setLikes(newLikes);

        return filmStorage.get(filmId);
    }

    /**
     * Return top ranked films.
     *
     * @param count limit to collection.
     * @return collection of Films.
     */
    @Override
    public Collection<Film> getTopFilms(Long count) {

        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingLong(Film::getLikes).reversed())
                .limit(count)
                .toList();
    }

    @Override
    public void throwIfNotFound(Long id) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }
}
