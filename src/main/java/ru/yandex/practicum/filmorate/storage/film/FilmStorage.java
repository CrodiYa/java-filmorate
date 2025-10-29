package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BasicStorage;

import java.util.Collection;

public interface FilmStorage extends BasicStorage<Film> {

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    Collection<Film> getTopFilms(Long count);
}
