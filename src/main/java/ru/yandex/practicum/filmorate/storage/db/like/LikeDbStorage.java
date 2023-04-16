package ru.yandex.practicum.filmorate.storage.db.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage{

    private static final String SQL_QUERY_LIKE_FILM = "INSERT INTO like (film_id, user_id) VALUES (?, ?)";
    private static final String SQL_QUERY_DISLIKE_FILM = "DELETE FROM like WHERE film_id = ? AND user_id = ?";
    private static final String SQL_QUERY_GET_ALL_FILM_LIKES = "SELECT user_id FROM like WHERE film_id = ?";
    private static final String USER_LIKE_FILM_INFO = "Пользователю {} нравится фильм {}";
    private static final String USER_DISLIKE_FILM_INFO = "Пользователю {} больше не нравится фильм {}";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void likeFilm(int filmId, int userId) {
        jdbcTemplate.update(SQL_QUERY_LIKE_FILM, filmId, userId);
        log.info(USER_LIKE_FILM_INFO, userId, filmId);
    }

    @Override
    public void dislikeFilm(int filmId, int userId) {
        jdbcTemplate.update(SQL_QUERY_DISLIKE_FILM, filmId, userId);
        log.info(USER_DISLIKE_FILM_INFO, userId, filmId);
    }

    @Override
    public List<Integer> getAllFilmLikes(int filmId) {
        return jdbcTemplate.query(SQL_QUERY_GET_ALL_FILM_LIKES, this::getIdFromRow);
    }

    private int getIdFromRow(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("FILM_ID");
    }


}
