package ru.yandex.practicum.filmorate.storage.db.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFriendRequestException;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage{

    private static final String SQL_QUERY_SEND_FRIEND_REQUEST = "INSERT INTO friendship (user1_id, user2_id) VALUES (?, ?)";
    private static final String SQL_QUERY_DELETE_FRIEND = "DELETE FROM friendship WHERE user1_id = ? and user2_id = ?";
    private static final String SENT_FRIEND_REQUEST_INFO = "Пользователь {} отправил запрос на дружбу пользователю {}.";
    private static final String DELETE_FROM_FRIENDS_INFO = "Пользователь {} больше не дружит с пользователем {}.";
    private static final String CONFIRM_REQUEST_INFO = "Пользователь {} подтвердил дружбу с пользователем {}.";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final UserStorage userDbStorage;

    @Override
    public void sendFriendRequest(int userId, int friendId) {
        jdbcTemplate.update(SQL_QUERY_SEND_FRIEND_REQUEST, userId, friendId);
        log.info(SENT_FRIEND_REQUEST_INFO, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FRIEND, userId, friendId);
        log.info(DELETE_FROM_FRIENDS_INFO, userId, friendId);
    }

    @Override
    public void confirmRequest(int userId, int friendId) { //TODO: Move to Service?
        List<Integer> requests = userDbStorage.getFriendRequests(userId);
        if (requests.contains(friendId)) {
            jdbcTemplate.update(SQL_QUERY_SEND_FRIEND_REQUEST, userId, friendId);
            log.info(CONFIRM_REQUEST_INFO, userId, friendId);
        } else {
            throw new NoSuchFriendRequestException(Constants.FRIEND_REQUEST_NOT_FOUND_EXCEPTION_INFO);
        }
    }
}
