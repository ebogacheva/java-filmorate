package ru.yandex.practicum.filmorate.storage.db.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    boolean sendFriendRequest(int userId, int friendId);

    boolean deleteFriend(int userId, int friendId);

    boolean confirmRequest(int userId, int friendId);

    List<Integer> getConfirmedFriends(int userId);

    List<Integer> getFriendRequests(int userId);

    List<User> getUserFriends(int userId);
}
