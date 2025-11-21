package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BasicStorage;

import java.util.Collection;

public interface FilmStorage extends BasicStorage<Film> {
    Collection<Film> getTopFilms(Long count);
}
