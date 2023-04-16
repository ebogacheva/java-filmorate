package ru.yandex.practicum.filmorate.storage.db.filmMpa;

import ru.yandex.practicum.filmorate.model.Mpa;

public interface FilmMpaStorage {

    void addFilmMpa(int filmId, int mpaId);

    Mpa getFilmMpaById(int filmId);
}