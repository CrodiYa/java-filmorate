package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmServiceInterface {

    Film getFilm(Long id);

    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long id);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    Collection<Film> getTopFilms(Long count);

    void throwIfNotFound(Long id);
}
