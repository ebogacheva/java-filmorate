package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FilmService {

    @Autowired
    FilmStorage filmStorage;

    public Film create (Film film) {
        return filmStorage.create(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film put(Film film) {
        if (Objects.isNull(filmStorage.getById(film.getId()))) {
            throw new NoSuchFilmException("Фильм не найден - обновление невозможно.");
        }
        return filmStorage.put(film);
    }

}
