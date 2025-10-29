package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of film storage.
 * <p>
 * Provides thread-safe storage for Film objects using ConcurrentHashMap with atomic ID generation.
 * Suitable for development and testing environments without persistent storage requirements.
 * </p>
 *
 * @see Film
 * @see FilmStorage
 */
@Component
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {

    private final Map<Long, Set<Long>> likes;

    public InMemoryFilmStorage() {
        super();
        likes = new ConcurrentHashMap<>();
    }

    @Override
    public Film add(Film film) {
        Film returnFilm = super.add(film);
        likes.put(film.getId(), new HashSet<>());

        return returnFilm;
    }

    @Override
    public Film remove(Long id) {
        Film film = super.remove(id);
        likes.remove(film.getId());

        return film;
    }

    @Override
    public void clear() {
        super.clear();
        likes.clear();
    }

    @Override
    public void throwIfNotFound(Long id) {
        if (!contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = get(filmId);

        Set<Long> filmLikes = likes.get(filmId);
        filmLikes.add(userId);
        film.setLikes((long) filmLikes.size());

        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        Film film = get(filmId);

        Set<Long> set = likes.get(filmId);
        set.remove(userId);
        film.setLikes((long) set.size());

        return film;
    }

    @Override
    public Collection<Film> getTopFilms(Long count) {
        return getAll().stream()
                .sorted(Comparator.comparingLong(Film::getLikes).reversed())
                .limit(count)
                .toList();
    }
}
