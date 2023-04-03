package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    Film create(Film film);
    List<Film> findAll();
    Film put(Film film);
    Film getById(int id);
}
