package ru.yandex.practicum.filmorate.storage.film.memory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of film films.
 * <p>
 * Provides thread-safe films for Film objects using ConcurrentHashMap with atomic ID generation.
 * Suitable for development and testing environments without persistent films requirements.
 * </p>
 *
 * @see Film
 * @see FilmStorage
 */
@Repository("MemFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films;
    private final AtomicLong idGenerator;

    public InMemoryFilmStorage() {
        this.films = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }

    @Override
    public Film add(Film film) {
        film.setId(idGenerator.getAndIncrement());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film update(Film newFilm) {
        films.put(newFilm.getId(), newFilm);

        return films.get(newFilm.getId());
    }

    @Override
    public Film remove(Long id) {
        return films.remove(id);
    }

    @Override
    public Film get(Long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return List.copyOf(films.values());
    }

    @Override
    public boolean contains(Long id) {
        return films.containsKey(id);
    }

    @Override
    public Collection<Film> getTopFilms(Long count) {
        return films.values().stream()
                .sorted(Comparator.comparingLong(Film::getLikes).reversed())
                .limit(count)
                .toList();
    }
}
