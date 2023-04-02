package ru.yandex.practicum.filmorate.service;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    UserStorage userStorage;

    public User create(User user) {
        String name = getCorrectName(user);
        User userWithCorrectName = user.withName(name);
        return userStorage.create(userWithCorrectName);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User put(User user) {
        checkUsersExistenceById(user.getId());
        String name = getCorrectName(user);
        User userUpdated = user.withName(name);
        return userStorage.put(userUpdated);
    }

    public void addFriend(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Теперь друзья: {} и {}", userId, friendId);
        //TODO: Надо что-то возвращать в теле?
    }

    public void deleteFriend(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Больше не друзья: {} и {}", userId, friendId);
        //TODO: Надо что-то возвращать в теле?
    }

    public List<User> friends(int userId) {
        checkUsersExistenceById(userId);
        Set<Integer> friendsId = userStorage.getById(userId).getFriends();
        return getFriendsList(new ArrayList<>(emptyIfNull(friendsId)));
    }

    public List<User> commonFriends(int userId, int otherId) {
        checkUsersExistenceById(userId, otherId);
        Collection<Integer> userFriends = emptyIfNull(userStorage.getById(userId).getFriends());
        Collection<Integer> otherFriends = emptyIfNull(userStorage.getById(otherId).getFriends());
        List<Integer> intersection = userFriends.stream().filter(otherFriends::contains).collect(Collectors.toList());
        return getFriendsList(intersection);
    }

    public User getById(int userId) {
        checkUsersExistenceById(userId);
        return userStorage.getById(userId);
    }

    private List<User> getFriendsList(List<Integer> ids) {
        List<User> friends = new ArrayList<>();
        for(Integer id : ids) {
            friends.add(userStorage.getById(id));
        }
        return friends;
    }

    public void checkUsersExistenceById(int...userIds) {
        for (int id : userIds) {
            if (Objects.isNull(userStorage.getById(id))) {
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
