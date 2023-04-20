package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Optional<Film> getById(int id);

    List<Film> findAll();

    Optional<Film> update(Film film);

    boolean delete(int id);

}
