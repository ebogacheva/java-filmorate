package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage{

    private static final String SQL_QUERY_GET_ALL_MPA = "SELECT mpa.id, mpa.name FROM mpa";
    private static final String SQL_QUERY_GET_MPA_BY_ID = "SELECT mpa.id, mpa.name FROM mpa WHERE id = ?";

    private static final String GOT_MPA_BY_ID_INFO = "Из базы получена ифнормация о рейтингу MPA {}.";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(int mpaId) {
        Mpa mpa = null;
        try {
            mpa = jdbcTemplate.queryForObject(SQL_QUERY_GET_MPA_BY_ID, MpaDbStorage::mapRowToMpa, mpaId);
            log.info(GOT_MPA_BY_ID_INFO, mpaId);
        } catch (DataAccessException ignored) {
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(SQL_QUERY_GET_ALL_MPA, MpaDbStorage::mapRowToMpa);
    }

    public static Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
