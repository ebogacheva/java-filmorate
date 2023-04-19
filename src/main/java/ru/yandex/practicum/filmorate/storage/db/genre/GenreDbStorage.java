package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.Constants;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private static final String SQL_QUERY_GET_GENRE_BY_ID =
            "SELECT genre.id, genre.name FROM genre WHERE id = ?";
    private static final String SQL_QUERY_GET_ALL_GENRES =
            "SELECT genre.id, genre.name FROM genre";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int genreId) {
        Genre genre = jdbcTemplate.queryForObject(SQL_QUERY_GET_GENRE_BY_ID, FilmorateRowMappers::getGenre, genreId);
        log.info(Constants.GOT_GENRE_BY_ID_LOG, genreId);
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        return new ArrayList<>(jdbcTemplate.query(SQL_QUERY_GET_ALL_GENRES, FilmorateRowMappers::getGenre));
    }
}
