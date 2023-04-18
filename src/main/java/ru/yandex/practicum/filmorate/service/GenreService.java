package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    @Autowired
    private final GenreStorage genreDbStorage;

    @Autowired
    private final FilmGenreStorage filmGenreDbStorage;

    public Genre getGenreById(int id) {
        Genre genre;
        try {
            genre = genreDbStorage.getGenreById(id);
        } catch (DataAccessException ex) {
            throw new NoSuchGenreException(Constants.GENRE_NOT_FOUND_EXCEPTION_INFO);
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return new ArrayList<>(genreDbStorage.getAllGenres());
    }

    public List<Genre> getAllFilmGenresById(int filmId) {
        return filmGenreDbStorage.getAllFilmGenresById(filmId);
    }

    public void addFilmGenre(int filmId, int genreId) {
        filmGenreDbStorage.addFilmGenre(filmId, genreId);
    }

}