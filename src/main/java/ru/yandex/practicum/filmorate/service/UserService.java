package ru.yandex.practicum.filmorate.service;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    public User getById(int userId) {
        User user;
        try {
            user = userDbStorage.getById(userId);
            log.info(Constants.GOT_USER_BY_ID_LOG, userId);
        } catch (DataAccessException ex) {
            log.warn(Constants.USER_NOT_FOUND_LOG, userId);
            throw new NoSuchUserException(Constants.USER_NOT_FOUND_EXCEPTION_INFO);
        }
        return user;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            users = userDbStorage.findAll();
        } catch (DataAccessException ignored) {
        }
        return users;
    }

    public User update(User user) {
        checkUsersExistenceById(user.getId());
        String name = getCorrectName(user);
        User userWithCorrectName = user.withName(name);
        User updated = userDbStorage.update(userWithCorrectName);
        log.info(Constants.UPDATED_USER_LOG, updated.getId());
        return updated;
    }

    public void delete(int id) {
        try {
            if (userDbStorage.delete(id)) {
                log.info(Constants.USER_DELETED_LOG, id);
            }
        } catch (DataAccessException ex) {
            log.warn(Constants.USER_NOT_FOUND_LOG, id);
            throw new NoSuchUserException(Constants.USER_NOT_FOUND_EXCEPTION_INFO);
        }
    }

    public void sendFriendRequest(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        friendshipDbStorage.sendFriendRequest(userId, friendId);
        log.info(Constants.SENT_FRIEND_REQUEST_LOG, userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        checkUsersExistenceById(userId, friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
        log.info(Constants.DELETE_FROM_FRIENDS_LOG, userId, friendId);
    }

    public List<User> friends(int userId) {
        checkUsersExistenceById(userId);
        List<User> friends = userDbStorage.getUserFriends(userId);
        log.info(Constants.SENT_FRIEND_REQUEST_LOG, userId, friends.size());
        return friends;
    }

    public List<User> confirmedFriends(int userId) {
        checkUsersExistenceById(userId);
        List<Integer> confirmedFriendsIds = friendshipDbStorage.getConfirmedFriends(userId);
        List<User> confirmedFriends = new ArrayList<>();
        confirmedFriendsIds.forEach(id -> confirmedFriends.add(getById(id)));
        return confirmedFriends;
    }

    public List<User> getFriendsRequests(int userId) {
        checkUsersExistenceById(userId);
        Set<Integer> requests = new HashSet<>(friendshipDbStorage.getFriendRequests(userId));
        log.info(Constants.USER_FRIENDS_REQUESTS_LOG, userId, requests.size());
        return getUsersList(new ArrayList<>(emptyIfNull(requests)));
    }

    public void confirmFriendRequest (int userId, int otherId) {
        checkUsersExistenceById(userId, otherId);
        friendshipDbStorage.confirmRequest(userId, otherId);
        log.info(Constants.CONFIRM_REQUEST_LOG, userId, otherId);
    }

    public List<User> commonFriends(int userId, int otherId) {
        checkUsersExistenceById(userId, otherId);
        Collection<User> userFriends = emptyIfNull(userDbStorage.getUserFriends(userId));
        Collection<User> otherFriends = emptyIfNull(userDbStorage.getUserFriends(otherId));
        List<User> intersection = userFriends.stream().filter(otherFriends::contains).collect(Collectors.toList());
        log.info(Constants.COMMON_FRIENDS_LOG, userId, otherId, intersection);
        return intersection;
    }

    public List<User> getUsersList(List<Integer> ids) {
        return findAll().stream()
                .filter(user -> ids.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public void checkUsersExistenceById(int...userIds) {
        for (int id : userIds) {
            getById(id);
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
