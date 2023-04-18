package ru.yandex.practicum.filmorate.storage.db.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface GenreStorage {

    Genre getGenreById(int genreId);
    List<Genre> getAllGenres();
}