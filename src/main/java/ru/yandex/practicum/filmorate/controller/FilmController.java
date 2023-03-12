package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping(value = "/films")
@RestController
@Slf4j
public class FilmController {

    protected static final AtomicInteger idProvider = new AtomicInteger(0);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (Validator.isValidated(film)) {
            int id = idProvider.incrementAndGet();
            Film filmCreated = film.withId(id);
            log.debug("Добавлен фильм: {}", filmCreated);
            films.put(id, filmCreated);
            return filmCreated;
        }
        return null;
    }

    @GetMapping
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchElementException();
        }
        if (Validator.isValidated(film)) {
            log.debug("Обновлены данные фильма: {}", film);
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

}
