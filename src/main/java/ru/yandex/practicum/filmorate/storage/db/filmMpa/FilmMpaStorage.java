package ru.yandex.practicum.filmorate.storage.db.filmMpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

public interface FilmMpaStorage {
    void setFilmMpa(int filmId, int mpaId);

    void deleteFilmMap(int filmId);

    Optional<Mpa> getFilmMpaById(int filmId);
}