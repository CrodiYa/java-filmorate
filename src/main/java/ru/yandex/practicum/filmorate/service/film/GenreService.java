package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {

    private final GenreStorage storage;

    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public Genre getGenre(Long id) {
        return storage.getGenre(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден"));
    }

    public Collection<Genre> getAll() {
        return storage.getAll();
    }
}
