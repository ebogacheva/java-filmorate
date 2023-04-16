package ru.yandex.practicum.filmorate.service;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserStorage userDbStorage;

    @Autowired
    FriendshipStorage friendshipDbStorage;

    public User create(User user) {
        String name = getCorrectName(user);
        User userWithCorrectName = user.withName(name);
        return userDbStorage.create(userWithCorrectName);
    }

    public List<User> findAll() {
        return userDbStorage.findAll();
    }

    public User update(User user) {
        checkUsersExistenceById(user.getId());
        String name = getCorrectName(user);
        User userUpdated = user.withName(name);
        log.info("Обновлен пользователь: {}", userUpdated.getId());
        return userDbStorage.update(userUpdated);
    }

    public void sendFriendRequest(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        friendshipDbStorage.sendFriendRequest(userId, friendId);
        log.info("Пользователь {} отправил запрос в друзья пользователю {}", userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", userId, friendId); //TODO: what with users' requests?
    }

    public List<User> friends(int userId) {
        checkUsersExistenceById(userId);
        Set<Integer> friendsId = new HashSet<>(userDbStorage.getUserFriends(userId));
        return getFriendsList(new ArrayList<>(emptyIfNull(friendsId)));
    }

    public List<User> commonFriends(int userId, int otherId) {
        checkUsersExistenceById(userId, otherId);
        Collection<Integer> userFriends = emptyIfNull(userDbStorage.getUserFriends(userId));
        Collection<Integer> otherFriends = emptyIfNull(userDbStorage.getUserFriends(otherId));
        List<Integer> intersection = userFriends.stream().filter(otherFriends::contains).collect(Collectors.toList());
        log.info("Общие друзья {} и {}: {}.", userId, otherId, intersection);
        return getFriendsList(intersection);
    }

    public User getById(int userId) {
        checkUsersExistenceById(userId);
        log.info("Найден пользователь: {}", userId);
        return userDbStorage.getById(userId);
    }

    private List<User> getFriendsList(List<Integer> ids) {
        return findAll().stream()
                .filter(user -> ids.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public void checkUsersExistenceById(int...userIds) {
        for (int id : userIds) {
            if (Objects.isNull(userDbStorage.getById(id))) {
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
