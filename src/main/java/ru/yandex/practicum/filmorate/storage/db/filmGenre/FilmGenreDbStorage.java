package ru.yandex.practicum.filmorate.storage.db.filmGenre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreDbStorage;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage{

    final static String SQL_QUERY_ADD_GENRE_FOR_FILM = "INSERT INTO film_genre (film_id, genre_id) values(?, ?)";
    final static String SQL_QUERY_GET_ALL_FILM_GENRES = "SELECT (g.id, d.name) FROM genre AS g RIGHT JOIN film_genre AS fm" +
            "ON g.id = fm.genre_id WHERE fm.film_id = ?";
    final static String NEW_GENRE_FOR_FILM_INFO = "Добавлен новый жанр {} к фильму {}.";
    final static String GOT_ALL_GENRES_FOR_FILM_INFO = "Из базы получены все жанры фильма {}.";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(int filmId, int genreId) {
        jdbcTemplate.update(SQL_QUERY_ADD_GENRE_FOR_FILM, filmId, genreId);
        log.info(NEW_GENRE_FOR_FILM_INFO, genreId, filmId);
    }

    @Override
    public Set<Genre> getAllFilmGenresById(int filmId) {
        Set<Genre> genres;
        try {
            genres = new HashSet<>(jdbcTemplate.query(SQL_QUERY_GET_ALL_FILM_GENRES, GenreDbStorage::mapRowToGenre, filmId));
            log.info(GOT_ALL_GENRES_FOR_FILM_INFO, filmId);
            return genres;
        } catch (DataAccessException ignored) {
            return Set.of();
        }
    }
}
