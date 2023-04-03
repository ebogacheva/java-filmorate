package ru.yandex.practicum.filmorate.service;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserStorage inMemoryUserStorage;

    public User create(User user) {
        String name = getCorrectName(user);
        User userWithCorrectName = user.withName(name);
        return inMemoryUserStorage.create(userWithCorrectName);
    }

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User put(User user) {
        checkUsersExistenceById(user.getId());
        String name = getCorrectName(user);
        User userUpdated = user.withName(name);
        log.info("Обновлен пользователь: {}", userUpdated.getId());
        return inMemoryUserStorage.put(userUpdated);
    }

    public void addFriend(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        User user = inMemoryUserStorage.getById(userId);
        User friend = inMemoryUserStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Теперь друзья: {} и {}", userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        User user = inMemoryUserStorage.getById(userId);
        User friend = inMemoryUserStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Больше не друзья: {} и {}", userId, friendId);
    }

    public List<User> friends(int userId) {
        checkUsersExistenceById(userId);
        Set<Integer> friendsId = inMemoryUserStorage.getById(userId).getFriends();
        log.info("У пользователя {} - {} друзей.", userId, friendsId.size());
        return getFriendsList(new ArrayList<>(emptyIfNull(friendsId)));
    }

    public List<User> commonFriends(int userId, int otherId) {
        checkUsersExistenceById(userId, otherId);
        Collection<Integer> userFriends = emptyIfNull(inMemoryUserStorage.getById(userId).getFriends());
        Collection<Integer> otherFriends = emptyIfNull(inMemoryUserStorage.getById(otherId).getFriends());
        List<Integer> intersection = userFriends.stream().filter(otherFriends::contains).collect(Collectors.toList());
        log.info("Общие друзья {} и {}: {}.", userId, otherId, intersection);
        return getFriendsList(intersection);
    }

    public User getById(int userId) {
        checkUsersExistenceById(userId);
        log.info("Найден пользователь: {}", userId);
        return inMemoryUserStorage.getById(userId);
    }

    private List<User> getFriendsList(List<Integer> ids) {
        List<User> friends = new ArrayList<>();
        for (Integer id : ids) {
            friends.add(inMemoryUserStorage.getById(id));
        }
        return friends;
    }

    public void checkUsersExistenceById(int...userIds) {
        for (int id : userIds) {
            if (Objects.isNull(inMemoryUserStorage.getById(id))) {
                log.info("Пользователь не найден: {}", id);
                throw new NoSuchUserException(Constants.USER_NOT_FOUND_INFO);
            }
        }
    }

    private String getCorrectName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            name = user.getLogin();
        }
        return name;
    }

}
