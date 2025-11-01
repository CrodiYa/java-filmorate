package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmServiceInterface;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmServiceInterface filmService;

    /**
     * Constructor for dependency injection
     */
    public FilmController(FilmServiceInterface filmService) {
        this.filmService = filmService;
    }

    /**
     * Handles GET method.
     * <p>Retrieves all filmsStorage from the storage.
     *
     * @return Collection of all filmsStorage.
     */
    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getAllFilms();
    }

    /**
     * Handles POST method.
     * <p>Creates film in storage after validation.
     *
     * @return created film.
     */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    /**
     * Handles PUT method.
     * <p>Updates film in storage after validation.
     *
     * <p>Conditions:
     * <ul>
     * <li>ID must be provided in the request body</li>
     * <li>Film with this ID should exist in the storage.</li>
     * </ul>
     *
     * @return updated film.
     * @throws ValidationException if ID is null or not valid
     * @throws NotFoundException   if film is not found
     */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        return filmService.updateFilm(newFilm);
    }

    /**
     * Handles GET method.
     * <p>Retrieves top {@code count} popular films based on likes.
     *
     * @param count must be provided by url params, not required default value is 10.
     * @return collection of films.
     */
    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(required = false, defaultValue = "10") @Positive Long count) {
        return filmService.getTopFilms(count);
    }

    /**
     * Handles PUT method.
     * <p>Adds like to specific film by specific user. If like from this user is already set - nothing happens.
     *
     * @param filmId Film`s id where likes amount should be increased. Must be positive number.
     * @param userId User`s id who sets like to the film. Must be positive number.
     * @return Film where like was set.
     */
    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable @Positive Long filmId,
                        @PathVariable @Positive Long userId) {
        return filmService.addLike(filmId, userId);
    }

    /**
     * Handles PUT method.
     * <p>Deletes like from specific film by specific user. If like was never there - nothing happens.
     *
     * @param filmId Film`s id where likes amount should be decreased. Must be positive number.
     * @param userId User`s id who deletes like to the film. Must be positive number.
     * @return Film where like was deleted.
     */
    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable @Positive Long filmId,
                           @PathVariable @Positive Long userId) {
        return filmService.removeLike(filmId, userId);
    }
}
