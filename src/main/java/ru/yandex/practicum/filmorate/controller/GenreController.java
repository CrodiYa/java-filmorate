package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {

    GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable @Positive Long id) {
        return service.getGenre(id);
    }

    @GetMapping
    public Collection<Genre> getAll() {
        return service.getAll();
    }
}