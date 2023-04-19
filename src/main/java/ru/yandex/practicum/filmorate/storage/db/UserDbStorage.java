package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

import java.util.*;

@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private static final String SQL_QUERY_GET_USER_BY_ID =
            "SELECT id, email, login, name, birthday " +
                    "FROM users_filmorate WHERE id = ?";
    private static final String SQL_QUERY_GET_ALL_USERS =
            "SELECT id, email, login, name, birthday  " +
            "FROM users_filmorate";
    private static final String SQL_QUERY_UPDATE_USER =
            "UPDATE users_filmorate SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String SQL_QUERY_DELETE_USER_BY_ID =
            "DELETE FROM users_filmorate WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS_FILMORATE")
                .usingGeneratedKeyColumns("ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        return user;
    }

    @Override
    public User getById(int id) {
        return jdbcTemplate.queryForObject(SQL_QUERY_GET_USER_BY_ID, FilmorateRowMappers::getUser, id);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_QUERY_GET_ALL_USERS, FilmorateRowMappers::getUser);
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(SQL_QUERY_UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(SQL_QUERY_DELETE_USER_BY_ID, id) > 0;
    }


}
