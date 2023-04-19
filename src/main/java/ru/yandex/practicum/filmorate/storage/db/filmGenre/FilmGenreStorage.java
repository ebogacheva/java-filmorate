package ru.yandex.practicum.filmorate.storage.db.filmGenre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {
    void addFilmGenre(int filmId, int genreId);
    boolean deleteFilmGenre(int filmId, int genreId);
    List<Genre> getAllFilmGenresById(int filmId);
}
