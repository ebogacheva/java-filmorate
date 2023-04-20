package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    Optional<User> getById(int id);

    Optional<User> update(User user);

    List<User> findAll();

    boolean delete(int id);
}
