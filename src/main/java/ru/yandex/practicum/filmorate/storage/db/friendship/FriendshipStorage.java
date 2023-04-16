package ru.yandex.practicum.filmorate.storage.db.friendship;

public interface FriendshipStorage {

    void sendFriendRequest(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    void confirmRequest(int userId, int friendId);
}
