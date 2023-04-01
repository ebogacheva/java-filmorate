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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping(value = "/users")
@RestController
@Slf4j
public class UserController {

    private static final AtomicInteger ID_PROVIDER = new AtomicInteger(0);
    private final HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        int id = ID_PROVIDER.incrementAndGet();
        String name = getCorrectName(user);
        User userCreated = user.withId(id).withName(name);
        log.debug("Добавлен пользователь: {}", userCreated);
        users.put(id, userCreated);
        return userCreated;
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchUserException("Пользователь не найден - обновление невозможно.");
        }
        String name = getCorrectName(user);
        User userUpdated = user.withName(name);
        log.debug("Обновлен пользователь: {}", userUpdated);
        users.put(userUpdated.getId(), userUpdated);
        return userUpdated;
    }

    private String getCorrectName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            name = user.getLogin();
        }
        return name;
    }
}
