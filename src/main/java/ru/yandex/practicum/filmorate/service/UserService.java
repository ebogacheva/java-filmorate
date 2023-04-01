package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserStorage userStorage;

    public User create(User user) {
        String name = getCorrectName(user);
        User userWithCorrectName = user.withName(name);
        return userStorage.put(userWithCorrectName);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User put(User user) {
        if (Objects.isNull(userStorage.getById(user.getId()))) {
            throw new NoSuchUserException("Пользователь не найден - обновление невозможно.");
        }
        String name = getCorrectName(user);
        User userUpdated = user.withName(name);
        return userStorage.put(userUpdated);
    }

    private String getCorrectName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            name = user.getLogin();
        }
        return name;
    }

}
