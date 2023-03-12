package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping(value = "/users")
@RestController
@Slf4j
public class UserController {

    protected static final AtomicInteger idProvider = new AtomicInteger(0);
    private final HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        if (Validator.isValidated(user)) {
            int id = idProvider.incrementAndGet();
            User userCreated = user.withId(id);
            log.debug("Добавлен пользователь: {}", userCreated);
            users.put(id, userCreated);
            return userCreated;
        }
        return null;
    }

    @GetMapping
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchUserException("Пользователь не найден - обновление невозможно.");
        }
        if (Validator.isValidated(user)) {
            log.debug("Обновлен пользователь: {}", user);
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

}
