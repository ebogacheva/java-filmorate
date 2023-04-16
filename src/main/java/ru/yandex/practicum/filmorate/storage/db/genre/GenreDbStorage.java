package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage{

    private static final String SQL_QUERY_GET_GENRE_BY_ID = "SELECT genre.id, genre.name FROM genre WHERE id = ?";
    private static final String SQL_QUERY_GET_ALL_GENRES = "SELECT genre.id, genre.name FROM genre";

    private static final String GOT_GENRE_BY_ID_INFO = "Из базы получена ифнормация о жанре {}.";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int genreId) {
        Genre genre = null;
        try {
            genre = jdbcTemplate.queryForObject(SQL_QUERY_GET_GENRE_BY_ID , GenreDbStorage::mapRowToGenre, genreId);
            log.info(GOT_GENRE_BY_ID_INFO, genreId);
        } catch (DataAccessException ignored) {
        }
        return genre;
    }

    @Override
    public Set<Genre> getAllGenres() {
        return new HashSet<>(jdbcTemplate.query(SQL_QUERY_GET_ALL_GENRES, GenreDbStorage::mapRowToGenre));
    }

    public static Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
