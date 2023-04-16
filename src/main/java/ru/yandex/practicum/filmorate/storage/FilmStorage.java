package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    Film create(Film film);
    Film getById(int id);
    List<Film> findAll();
    Film update(Film film);
    boolean delete(int id);

}
