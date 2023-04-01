package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (notExist(user.getId())) {
            throw new NoSuchUserException(Constants.USER_NOT_FOUND_INFO);
        }
        String name = getCorrectName(user);
        User userUpdated = user.withName(name);
        return userStorage.put(userUpdated);
    }

    public void addFriend(int userId, int friendId) {
        if (notExist(userId) && notExist(friendId)) {
            throw new NoSuchUserException(Constants.USER_NOT_FOUND_INFO);
        }
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        //TODO: Надо что-то возвращать в теле?
    }

    public void deleteFriend(int userId, int friendId) {
        if (notExist(userId) && notExist(friendId)) {
            throw new NoSuchUserException(Constants.USER_NOT_FOUND_INFO);
        }
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        //TODO: Надо что-то возвращать в теле?
    }

    public List<User> friends(int userId) {
        Set<Integer> friendsId = userStorage.getById(userId).getFriends();
        if (Objects.isNull(friendsId)) {
            return null;
        }
        return getFriendsList(friendsId);
    }

    public List<User> commonFriends(int userId, int otherId) {
        Set<Integer> userFriends = userStorage.getById(userId).getFriends();
        Set<Integer> otherFriends = userStorage.getById(otherId).getFriends();
        if (Objects.isNull(userFriends) || Objects.isNull(otherFriends)) {
            return null;
        }
        Set<Integer> intersection = userFriends.stream().filter(otherFriends::contains).collect(Collectors.toSet());
        return getFriendsList(intersection);
    }

    private List<User> getFriendsList(Set<Integer> ids) {
        List<User> friends = new ArrayList<>();
        for(Integer id : ids) {
            friends.add(userStorage.getById(id));
        }
        return friends;
    }

    private boolean notExist(int userId) {
        return Objects.isNull(userStorage.getById(userId));
    }

    private String getCorrectName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            name = user.getLogin();
        }
        return name;
    }

}
