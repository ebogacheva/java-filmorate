package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    //TODO: refactor
    private final String SQL_QUERY_GET_USER_BY_ID = "SELECT * FROM users_filmorate WHERE user_id = ?";
    private final String SQL_QUERY_GET_ALL_USERS = "SELECT * FROM users_filmorate";
    private final String SQL_QUERY_UPDATE = "UPDATE users_filmorate SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?";
    private final String SQL_QUERY_DELETE_FRIENDS = "DELETE FROM friendship WHERE user1_id = ?";
    private final String SQL_QUERY_INSERT_FRIENDS = "INSERT INTO friendship (user1_id, user2_id, approved) values (?, ?, 'TRUE')";
    private final String SQL_QUERY_DELETE_USER_BY_ID = "DELETE FROM users_filmorate WHERE id = ?";

    private final String SQL_QUERY_GET_CONFIRMED_FRIENDS = "SELECT * FROM users_filmorate WHERE user_id IN " +
            "(SELECT f1.user2_id FROM friendship AS f1 JOIN friendship AS f2 " +
            "ON f1.user1_id = ? AND f1.user2_id = f2.user1_id)";

    private final String SQL_QUERY_GET_FRIENDS_REQUESTS = "SELECT * FROM users_filmorate WHERE user_id IN " +
            "(SELECT user1_id FROM friendship WHERE user2_id = ?) AND NOT IN" +
            "(SELECT f1.user1_id FROM friendship AS f1 JOIN friendship AS f2 " +
            "ON f1.user2_id = f2.user1_id AND f1.user1_id = ?";


    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS_FILMORATE")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        return user;
    }

    @Override
    public User getById(int id) {
        return jdbcTemplate.queryForObject(SQL_QUERY_GET_USER_BY_ID, this::mapRowToUser, id);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_QUERY_GET_ALL_USERS, this::mapRowToUser);
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(SQL_QUERY_UPDATE,
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
    public List<Integer> getUserFriends(int id) {
        return jdbcTemplate.query(SQL_QUERY_GET_CONFIRMED_FRIENDS,this::getIdFromRow, id);
    }

    @Override
    public List<Integer> getFriendRequests(int id) {
        return jdbcTemplate.query(SQL_QUERY_GET_FRIENDS_REQUESTS, this::getIdFromRow, id);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    private int getIdFromRow(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("USER_ID");
    }
}
