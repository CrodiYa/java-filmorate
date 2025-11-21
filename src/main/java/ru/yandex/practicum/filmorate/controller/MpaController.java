package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService service;

    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable @Positive Long id) {
        return service.getMpa(id);
    }

    @GetMapping
    public Collection<Mpa> getAll() {
        return service.getAll();
    }
}
