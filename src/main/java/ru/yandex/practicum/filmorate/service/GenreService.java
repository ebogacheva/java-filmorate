package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {

    @Autowired
    private final GenreStorage genreDbStorage;

    public Genre getGenreById(int id) {
        Genre genre;
        try {
            genre = genreDbStorage.getGenreById(id);
        } catch (DataAccessException ex) {
            throw new NoSuchGenreException(Constants.GENRE_NOT_FOUND_INFO);
        }
        return genre;
    }

    public Set<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }
}