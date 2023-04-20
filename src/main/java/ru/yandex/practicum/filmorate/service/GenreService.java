package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmorateElementException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreDbStorage;
    private final FilmGenreStorage filmGenreDbStorage;

    public Genre getGenreById(int id) {
        Optional<Genre> genre = genreDbStorage.getGenreById(id);
        if (genre.isPresent()) {
            return genre.get();
        } else throw new NoSuchFilmorateElementException(Constants.GENRE_NOT_FOUND_EXCEPTION_INFO);
    }

    public List<Genre> getAllGenres() {
        return new ArrayList<>(genreDbStorage.getAllGenres());
    }

    public void addFilmGenre(int filmId, int genreId) {
        filmGenreDbStorage.addFilmGenre(filmId, genreId);
    }

    public void deleteFilmGenre(int filmId, int genreId) {
        filmGenreDbStorage.deleteFilmGenre(filmId, genreId);
    }

    public List<Genre> getAllFilmGenresById(int filmId) {
        return filmGenreDbStorage.getAllFilmGenresById(filmId);
    }
}