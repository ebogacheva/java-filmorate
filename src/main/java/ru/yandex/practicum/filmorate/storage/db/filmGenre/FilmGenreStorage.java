package ru.yandex.practicum.filmorate.storage.db.filmGenre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmGenreStorage {
    void addFilmGenre(int filmId, int genreId);
    void deleteFilmGenre(int filmId);
    List<Genre> getAllFilmGenresById(int filmId);
}
