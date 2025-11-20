package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

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
@Transactional
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;

    /**
     * Constructor for dependency injection
     */
    public FilmService(@Qualifier("DbFilmStorage") FilmStorage filmStorage,
                       @Qualifier("DbUserStorage") UserStorage userStorage,
                       @Qualifier("DbLikeStorage") LikeStorage likeStorage,
                       @Qualifier("DbGenreStorage") GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
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
        film = filmStorage.add(film);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreStorage.addGenresToFilm(film);
        }

        return film;
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

        film = filmStorage.update(film);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreStorage.updateFilmGenres(film);
        }

        return film;
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
        likeStorage.clearLikes(id);
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
        Film film = filmStorage.get(filmId);
        film.setLikes(newLikes);

        return film;
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
        Film film = filmStorage.get(filmId);
        film.setLikes(newLikes);

        return film;
    }

    /**
     * Return top ranked films.
     *
     * @param count limit to collection.
     * @return collection of Films.
     */
    @Override
    public Collection<Film> getTopFilms(Long count) {
        return filmStorage.getTopFilms(count);
    }

    @Override
    public void throwIfNotFound(Long id) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }
}
