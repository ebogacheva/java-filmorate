package ru.yandex.practicum.filmorate.storage.db.filmGenre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage{

    private final static String SQL_QUERY_ADD_GENRE_FOR_FILM = "INSERT INTO film_genre (film_id, genre_id) values(?, ?)";
    private final static String SQL_QUERY_DELETE_FILM_GENRES = "DELETE FROM film_genre WHERE film_id = ?";
    private final static String SQL_QUERY_GET_ALL_FILM_GENRES = "SELECT g.id, g.name FROM genre AS g LEFT JOIN film_genre AS fg " +
            "ON g.id = fg.genre_id WHERE fg.film_id = ?";
    private final static String NEW_GENRE_FOR_FILM_INFO = "Добавлен новый жанр {} к фильму {}.";
    private final static String GOT_ALL_GENRES_FOR_FILM_INFO = "Из базы получены все жанры фильма {}.";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(int filmId, int genreId) {
        jdbcTemplate.update(SQL_QUERY_ADD_GENRE_FOR_FILM, filmId, genreId);
        log.info(NEW_GENRE_FOR_FILM_INFO, genreId, filmId);
    }

    @Override
    public void deleteFilmGenre(int filmId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FILM_GENRES, filmId);
    }

    @Override
    public List<Genre> getAllFilmGenresById(int filmId) {
        List<Genre> genres;
        try {
            genres = jdbcTemplate.query(SQL_QUERY_GET_ALL_FILM_GENRES, FilmorateRowMappers::getGenre, filmId);
            log.info(GOT_ALL_GENRES_FOR_FILM_INFO, filmId);
            return genres;
        } catch (DataAccessException ignored) {
            return List.of();
        }
    }
}
