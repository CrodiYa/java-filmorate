package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final HashMap<Long, Film> films = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    /**
     * Private constructor to initialize ID generator with starting ID = 1.
     */
    private FilmController() {
        idGenerator.set(1);
    }

    /**
     * Handles GET method.
     * <p>Retrieves all films from the storage.
     *
     * @return Collection of all films.
     */
    @GetMapping
    public Collection<Film> getFilms() {
        log.info("GET /films - returning {} films", films.size());
        return films.values();
    }

    /**
     * Handles POST method.
     * <p>Creates film in storage after validation.
     *
     * @return created film.
     */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        film.setId(idGenerator.getAndIncrement());
        films.put(film.getId(), film);

        log.info("POST /films - Film created: {}", film);

        return film;
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

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            films.put(newFilm.getId(), newFilm);
            log.info("PUT /films - Film updated: {}", oldFilm);

            return newFilm;
        }

        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }
}
