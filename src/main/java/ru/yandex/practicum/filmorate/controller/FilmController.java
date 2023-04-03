package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = "/films")
@RestController
@Slf4j
public class FilmController {

    @Autowired
    FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Film getById(@PathVariable int id) {
        return filmService.getById(id);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        return filmService.put(film);
    }

    @PutMapping(value = "{id}/like/{userId}")
    public Film put(@PathVariable int id,
                    @PathVariable int userId) {
        return filmService.like(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film delete(@PathVariable int id,
                       @PathVariable int userId) {
        return filmService.dislike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> get(@RequestParam(defaultValue = "10") int count) {
        return filmService.popular(count);
    }
}
