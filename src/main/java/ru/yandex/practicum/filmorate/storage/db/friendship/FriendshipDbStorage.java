package ru.yandex.practicum.filmorate.storage.db.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFriendRequestException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Constants;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private static final String SQL_QUERY_SEND_FRIEND_REQUEST =
            "INSERT INTO friendship (user1_id, user2_id) VALUES (?, ?)";
    private static final String SQL_QUERY_DELETE_FRIEND =
            "DELETE FROM friendship WHERE user1_id = ? and user2_id = ?";
    private static final String SQL_QUERY_GET_CONFIRMED_FRIENDS =
            "SELECT f1.user2_id FROM friendship AS f1 " +
                    "JOIN friendship AS f2 ON f1.user2_id = f2.user1_id " +
                    "AND f2.user2_id = f1.user1_id WHERE f1.user1_id = ?";
    private static final String SQL_QUERY_GET_FRIENDS_REQUESTS =
            "SELECT user1_id FROM friendship WHERE user2_id = ? " +
                    "AND user1_id NOT IN " +
            "(SELECT f1.user2_id FROM friendship AS f1 " +
                    "JOIN friendship AS f2 ON f1.user2_id = f2.user1_id " +
                    "AND f2.user2_id = f1.user1_id WHERE f1.user1_id = ?)";
    private static final String SQL_QUERY_GET_USER_FRIENDS =
            "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                    "FROM users_filmorate AS u " +
                    "RIGHT JOIN friendship AS f ON u.id = f.user2_id " +
                    "WHERE f.user1_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final UserStorage userDbStorage;

    @Override
    public List<User> getUserFriends(int userId) {
        return jdbcTemplate.query(SQL_QUERY_GET_USER_FRIENDS, FilmorateRowMappers::getUser, userId);
    }

    @Override
    public void sendFriendRequest(int userId, int friendId) {
        jdbcTemplate.update(SQL_QUERY_SEND_FRIEND_REQUEST, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FRIEND, userId, friendId);
    }

    @Override
    public void confirmRequest(int userId, int friendId) {
        List<Integer> requests = getFriendRequests(userId);
        if (requests.contains(friendId)) {
            jdbcTemplate.update(SQL_QUERY_SEND_FRIEND_REQUEST, userId, friendId);
        } else {
            throw new NoSuchFriendRequestException(Constants.FRIEND_REQUEST_NOT_FOUND_EXCEPTION_INFO);
        }
    }
    @Override
    public List<Integer> getFriendRequests(int userId) {
        return jdbcTemplate.query(SQL_QUERY_GET_FRIENDS_REQUESTS, FilmorateRowMappers::getIdForUser1, userId, userId);
    }

    @Override
    public List<Integer> getConfirmedFriends(int userId) {
        return jdbcTemplate.query(SQL_QUERY_GET_CONFIRMED_FRIENDS, FilmorateRowMappers::getIdForUser2, userId);
    }
}
