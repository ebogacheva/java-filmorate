package ru.yandex.practicum.filmorate.storage.db.friendship;

import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface FriendshipStorage {

    void sendFriendRequest(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    void confirmRequest(int userId, int friendId);
    List<Integer> getConfirmedFriends(int userId);
    List<Integer> getFriendRequests(int userId);
}
