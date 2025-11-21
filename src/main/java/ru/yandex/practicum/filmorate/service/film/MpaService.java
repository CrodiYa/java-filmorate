package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.Collection;

@Service
public class MpaService {

    private final MpaStorage storage;

    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    public Mpa getMpa(Long id) {
        return storage.getMpa(id)
                .orElseThrow(() -> new NotFoundException("Mpa с id = " + id + " не найден"));
    }

    public Collection<Mpa> getAll() {
        return storage.getAll();
    }
}
