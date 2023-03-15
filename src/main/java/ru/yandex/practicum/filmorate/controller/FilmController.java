package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping(value = "/films")
@RestController
@Slf4j
public class FilmController {

    private static final AtomicInteger ID_PROVIDER = new AtomicInteger(0);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        int id = ID_PROVIDER.incrementAndGet();
        Film filmCreated = film.withId(id);
        log.debug("Добавлен фильм: {}", filmCreated);

        films.put(id, filmCreated);
        return filmCreated;
    }

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchFilmException("Фильм не найден - обновление невозможно.");
        }
        log.debug("Обновлены данные фильма: {}", film);
        films.put(film.getId(), film);
        return film;
    }

}
