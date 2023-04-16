package ru.yandex.practicum.filmorate.storage.db.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFriendRequestException;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage{

    private final String SQL_QUERY_SEND_FRIEND_REQUEST = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
    private final String SQL_QUERY_DELETE_FRIEND = "DELETE FROM friendships WHERE user_id = ? and friend_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final UserStorage userDbStorage;

    @Override
    public void sendFriendRequest(int userId, int friendId) {
        jdbcTemplate.update(SQL_QUERY_SEND_FRIEND_REQUEST, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FRIEND, userId, friendId);
    }

    @Override
    public void confirmRequest(int userId, int friendId) { //TODO: Move to Service?
        List<Integer> requests = userDbStorage.getFriendRequests(userId);
        if (requests.contains(friendId)) {
            jdbcTemplate.update(SQL_QUERY_SEND_FRIEND_REQUEST, userId, friendId);
        } else {
            throw new NoSuchFriendRequestException(Constants.FRIEND_REQUEST_NOT_FOUND);
        }
    }
}
