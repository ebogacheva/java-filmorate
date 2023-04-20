package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private static final String SQL_QUERY_GET_ALL_MPA = "SELECT id, name FROM mpa";
    private static final String SQL_QUERY_GET_MPA_BY_ID = "SELECT id, name FROM mpa WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> getMpaById(int mpaId) {
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(SQL_QUERY_GET_MPA_BY_ID, FilmorateRowMappers::getMpa, mpaId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(mpa);
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(SQL_QUERY_GET_ALL_MPA, FilmorateRowMappers::getMpa);
    }
}
