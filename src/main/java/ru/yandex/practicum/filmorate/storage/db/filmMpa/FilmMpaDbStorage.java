package ru.yandex.practicum.filmorate.storage.db.filmMpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmMpaDbStorage implements FilmMpaStorage{

    private static final String SQL_QUERY_GET_MPA_BY_FILM_ID =
            "SELECT mpa.id, mpa.name " +
            "FROM film_mpa LEFT JOIN mpa " +
            "ON film_mpa.mpa_id = mpa.id WHERE film_id = ?";
    private static final String SQL_QUERY_ADD_FILM_MPA = "INSERT INTO film_mpa (film_id, mpa_id) VALUES (?, ?)";
    private static final String SQL_QUERY_DELETE_FILM_MPA = "DELETE FROM film_mpa WHERE film_id = ?";

    private static final String SAVED_FILM_MPA_INFO = "Сохранен рейтинг MPA {} для фильма {}.";
    private static final String GOT_FILM_MPA_INFO = "Из базы получен рейтинг MPA для фильма {}.";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmMpa(int filmId, int mpaId) {
        jdbcTemplate.update(SQL_QUERY_ADD_FILM_MPA, filmId, mpaId);
        log.info(SAVED_FILM_MPA_INFO, mpaId, filmId);
    }

    @Override
    public void deleteFilmMap(int filmId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FILM_MPA, filmId);
    }

    @Override
    public Mpa getFilmMpaById(int filmId) {
        Mpa mpa = null;
        try {
            mpa = jdbcTemplate.queryForObject(SQL_QUERY_GET_MPA_BY_FILM_ID,  FilmorateRowMappers::getMpa, filmId);
            log.info(GOT_FILM_MPA_INFO, filmId);
        } catch (DataAccessException ignored) {
        }
        return mpa;
    }

}
