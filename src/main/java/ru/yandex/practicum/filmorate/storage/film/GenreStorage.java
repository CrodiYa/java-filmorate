package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {

    Optional<Genre> getGenre(Long id);

    Collection<Genre> getAll();

    void addGenresToFilm(Film film);

    void updateFilmGenres(Film film);
}
