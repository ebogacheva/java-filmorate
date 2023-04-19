package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.db.filmMpa.FilmMpaStorage;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

import java.sql.PreparedStatement;
import java.util.*;

@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final String SQL_QUERY_CREATE_FILM =
            "INSERT INTO film (name, description, release_date, duration) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SQL_QUERY_GET_FILM_BY_ID =
            "SELECT id, name, description, release_date, duration " +
            "FROM film WHERE id = ?";
    private static final String SQL_QUERY_GET_ALL_FILMS =
            "SELECT id, name, description, release_date, duration " +
            "FROM film";
    private static final String SQL_QUERY_UPDATE_FILM =
            "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ? " +
            "WHERE id = ?";
    private static final String SQL_QUERY_DELETE_FILM_BY_ID =
            "DELETE FROM film WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreDbStorage;
    private final FilmMpaStorage filmMpaDbStorage;


    @Override
    public Film create(Film film) {

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_CREATE_FILM, new String[]{"id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setObject(3, film.getReleaseDate());
            preparedStatement.setLong(4, film.getDuration());
            return preparedStatement;
        }, generatedKeyHolder);

        int filmId = Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();
        Film created = film.withId(filmId);
        completeDbWithFilmMpaAndGenres(created);
        completeFilmWithMpaAndGenresFromDb(created);
        return created;
    }

    @Override
    public Film getById(int id) {
        Film film = jdbcTemplate.queryForObject(SQL_QUERY_GET_FILM_BY_ID, FilmorateRowMappers::getFilm, id);
        if (Objects.nonNull(film)) {
            completeFilmWithMpaAndGenresFromDb(film);
        }
        return film;
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = jdbcTemplate.query(SQL_QUERY_GET_ALL_FILMS, FilmorateRowMappers::getFilm);
        films.forEach(this::completeFilmWithMpaAndGenresFromDb);
        return films;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(SQL_QUERY_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        completeDbWithFilmMpaAndGenres(film);
        return film;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(SQL_QUERY_DELETE_FILM_BY_ID, id) > 0;
    }

    private void completeDbWithFilmMpaAndGenres(Film film) {
        int filmId = film.getId();

        clearDbFilmMpaAndGenre(filmId);

        if (Objects.nonNull(film.getMpa())){
            int mpaId = film.getMpa().getId();
            filmMpaDbStorage.setFilmMpa(filmId, mpaId);
        }
        if (Objects.nonNull(film.getGenres())) {
            Set<Genre> genres = film.getGenres();
            genres.forEach(genre -> filmGenreDbStorage.addFilmGenre(filmId, genre.getId()));
        }
    }

    private void completeFilmWithMpaAndGenresFromDb(Film film) {
        int filmId = film.getId();
        Mpa mpa = filmMpaDbStorage.getFilmMpaById(filmId);
        Set<Genre> genres = new TreeSet<>(filmGenreDbStorage.getAllFilmGenresById(filmId));
        film.setMpa(mpa);
        film.setGenres(genres);
    }

    private void clearDbFilmMpaAndGenre(int filmId) {
        filmMpaDbStorage.deleteFilmMap(filmId);
        List<Genre> genres = filmGenreDbStorage.getAllFilmGenresById(filmId);
        genres.forEach(genre -> filmGenreDbStorage.deleteFilmGenre(filmId, genre.getId()));
    }

}
