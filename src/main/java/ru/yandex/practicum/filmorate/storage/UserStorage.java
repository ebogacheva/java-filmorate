package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);
    User getById(int id);
    User update(User user);
    List<User> findAll();
    boolean delete(int id);
    List<User> getUserFriends(int id);
}
