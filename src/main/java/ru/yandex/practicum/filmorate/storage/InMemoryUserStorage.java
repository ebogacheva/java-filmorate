package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private static final AtomicInteger ID_PROVIDER = new AtomicInteger(0);
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        int id = ID_PROVIDER.incrementAndGet();
        User userCreated = user.withId(id);
        log.debug("Добавлен пользователь: {}", userCreated);
        users.put(id, userCreated);
        return userCreated;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User put(User user) {
        log.debug("Обновлен пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }
}
