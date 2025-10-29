package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

/**
 * Service for managing film likes functionality.
 * <p>
 * Handles operations related to film likes including adding, removing likes and retrieving top-liked films.
 * Maintains an internal concurrent storage for tracking user likes for each film.
 * </p>
 *
 * @see Film
 * @see FilmStorage
 * @see UserStorage
 */
@Service
public class FilmLikeService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    /**
     * Constructor for dependency injection
     */
    public FilmLikeService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /**
     * Adds like to film.
     * <p> Adds userId in set of ids for specific film.
     * Sets likes value in Film to set size.
     *
     * @param filmId film`s id to find set of likes.
     * @param userId user`s id to add to set of likes.
     * @return Film where like was added.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user or films is not present in storage.
     */
    public Film addLike(Long filmId, Long userId) {
        userStorage.throwIfNotFound(userId);

        return filmStorage.addLike(filmId, userId);
    }

    /**
     * Remove like from film.
     * <p> Remove userId from set of ids for specific film.
     * Sets likes value in Film to set size.
     *
     * @param filmId film`s id to find set of likes.
     * @param userId user`s id to delete from set of likes.
     * @return Film where like was deleted.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user or films is not present in storage.
     */
    public Film removeLike(Long filmId, Long userId) {
        userStorage.throwIfNotFound(userId);

        return filmStorage.removeLike(filmId, userId);
    }

    /**
     * Return top-liked films with limit.
     * <p> Streams through all films and sorts them by their likes amount.
     *
     * @param count limit to returned collection.
     * @return collection of Films.
     */
    public Collection<Film> getTopFilms(Long count) {
        return filmStorage.getTopFilms(count);
    }
}
