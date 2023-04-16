package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User create(User user);
    User getById(int id);
    User update(User user);
    List<User> findAll();

    boolean delete(int id);

    List<Integer> getUserFriends(int id);

    List<Integer> getCommonFriends(int id1, int id2);

    List<Integer> getFriendRequests(int id);
}
