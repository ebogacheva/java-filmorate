package ru.yandex.practicum.filmorate.storage.db.filmGenre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {

    private static final String SQL_QUERY_ADD_GENRE_FOR_FILM = "INSERT INTO film_genre (film_id, genre_id) values(?, ?)";
    private static final String SQL_QUERY_DELETE_FILM_GENRE = "DELETE FROM film_genre WHERE film_id = ? and genre_id = ?";
    private static final String SQL_QUERY_GET_ALL_FILM_GENRES =
            "SELECT g.id, g.name FROM genre AS g " +
                    "LEFT JOIN film_genre AS fg ON g.id = fg.genre_id WHERE fg.film_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(int filmId, int genreId) {
        jdbcTemplate.update(SQL_QUERY_ADD_GENRE_FOR_FILM, filmId, genreId);
    }

    @Override
    public void deleteFilmGenre(int filmId, int genreId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FILM_GENRE, filmId, genreId);
    }

    @Override
    public List<Genre> getAllFilmGenresById(int filmId) {
        return jdbcTemplate.query(SQL_QUERY_GET_ALL_FILM_GENRES, FilmorateRowMappers::getGenre, filmId);
    }
}
