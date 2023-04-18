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

    private static final String SQL_QUERY_GET_USER_BY_ID = "SELECT * FROM users_filmorate WHERE id = ?";
    private static final String SQL_QUERY_GET_ALL_USERS = "SELECT * FROM users_filmorate";
    private static final String SQL_QUERY_UPDATE_USER = "UPDATE users_filmorate SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String SQL_QUERY_DELETE_USER_BY_ID = "DELETE FROM users_filmorate WHERE id = ?";

    private static final String SQL_QUERY_GET_CONFIRMED_FRIENDS = "SELECT f1.user2_id FROM friendship AS f1 " +
            "JOIN friendship AS f2 ON f1.user2_id = f2.user1_id WHERE f1.user1_id = ?";

    private static final String SQL_QUERY_GET_USER_FRIENDS = "SELECT u.id, u.email, u.login, u.name, u.birthday FROM users_filmorate AS u RIGHT JOIN " +
            "friendship AS f ON u.id = f.user2_id WHERE f.user1_id = ?";

    private static final String SQL_QUERY_GET_FRIENDS_REQUESTS = "SELECT user1_id FROM friendship WHERE user2_id = ? AND NOT IN" +
            "SELECT f1.user2_id FROM friendship AS f1 " +
            "JOIN friendship AS f2 ON f1.user2_id = f2.user1_id WHERE f1.user1_id = ?";

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

    @Override
    public List<User> getUserFriends(int id) {
        return jdbcTemplate.query(SQL_QUERY_GET_USER_FRIENDS, FilmorateRowMappers::getUser, id);
    }

    @Override
    public List<Integer> getFriendRequests(int id) {
        return jdbcTemplate.query(SQL_QUERY_GET_FRIENDS_REQUESTS, FilmorateRowMappers::getIdForUser, id);
    }
}
