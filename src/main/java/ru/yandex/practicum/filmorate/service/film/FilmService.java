package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

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
 * @see FilmLikeService
 */
@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final FilmLikeService filmLikeService;

    /**
     * Constructor for dependency injection
     */
    public FilmService(FilmStorage filmStorage, FilmLikeService filmLikeService) {
        this.filmStorage = filmStorage;
        this.filmLikeService = filmLikeService;
    }

    /**
     * Retrieves specific film from the storage.
     *
     * @param id Film`s id to get.
     * @return Film.
     */
    public Film getFilm(Long id) {
        return filmStorage.get(id);
    }

    /**
     * Retrieves all filmsStorage from the storage.
     *
     * @return Collection of all filmsStorage.
     */
    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    /**
     * Adds film to the storage.
     *
     * @param film Film to add.
     * @return created Film.
     */
    public Film addFilm(Film film) {
        return filmStorage.add(film);
    }

    /**
     * Updates film in the storage.
     *
     * @param film Film to update.
     * @return updated Film.
     */
    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    /**
     * Deletes film from the storage.
     *
     * @param id Film`s id to delete.
     */
    public void deleteFilm(Long id) {
        filmStorage.remove(id);
    }

    /**
     * Adds like to the film.
     *
     * @param filmId Film`s id to add like.
     * @param userId User`s id to set like.
     * @return Film where likes where added.
     */
    public Film addLike(Long filmId, Long userId) {
        return filmLikeService.addLike(filmId, userId);
    }

    /**
     * Deletes like from the film.
     *
     * @param filmId Film`s id to delete like.
     * @param userId User`s id to delete like.
     * @return Film where likes where deleted.
     */
    public Film removeLike(Long filmId, Long userId) {
        return filmLikeService.removeLike(filmId, userId);
    }

    /**
     * Return top ranked films.
     *
     * @param count limit to collection.
     * @return collection of Films.
     */
    public Collection<Film> getTopFilms(Long count) {
        return filmLikeService.getTopFilms(count);
    }

}
