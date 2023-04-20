package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotPerformedFilmorateOperationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Slf4j
@Service
public class FriendshipService {

    @Autowired
    FriendshipStorage friendshipDbStorage;

    @Autowired
    UserService userService;

    public void sendFriendRequest(int userId, int friendId) {
        userService.checkUsersExistenceById(userId, friendId);
        if (friendshipDbStorage.sendFriendRequest(userId, friendId)) {
            log.info(Constants.SENT_FRIEND_REQUEST_LOG, userId, friendId);
        } else {
            throw new NotPerformedFilmorateOperationException(Constants.ADD_FRIEND_NOT_PERFORMED_EXCEPTION_INFO);
        }
    }

    public void deleteFriend(int userId, int friendId) {
        userService.checkUsersExistenceById(userId, friendId);
        if (friendshipDbStorage.deleteFriend(userId, friendId)) {
            log.info(Constants.DELETE_FROM_FRIENDS_LOG, userId, friendId);
        } else {
            throw new NotPerformedFilmorateOperationException(Constants.FRIEND_DELETE_NOT_PERFORMED_EXCEPTION_INFO);
        }
    }

    public List<User> friends(int userId) {
        userService.checkUsersExistenceById(userId);
        List<User> friends = friendshipDbStorage.getUserFriends(userId);
        log.info(Constants.SENT_FRIEND_REQUEST_LOG, userId, friends.size());
        return friends;
    }

    public List<User> confirmedFriends(int userId) {
        userService.checkUsersExistenceById(userId);
        List<Integer> confirmedFriendsIds = friendshipDbStorage.getConfirmedFriends(userId);
        List<User> confirmedFriends = new ArrayList<>();
        confirmedFriendsIds.forEach(id -> confirmedFriends.add(userService.getById(id)));
        return confirmedFriends;
    }

    public List<User> getFriendsRequests(int userId) {
        userService.checkUsersExistenceById(userId);
        Set<Integer> requests = new HashSet<>(friendshipDbStorage.getFriendRequests(userId));
        log.info(Constants.USER_FRIENDS_REQUESTS_LOG, userId, requests.size());
        return userService.getUsersList(new ArrayList<>(emptyIfNull(requests)));
    }

    public void confirmFriendRequest(int userId, int otherId) {
        userService.checkUsersExistenceById(userId, otherId);
        if (friendshipDbStorage.confirmRequest(userId, otherId)) {
            log.info(Constants.CONFIRM_REQUEST_LOG, userId, otherId);
        } else {
            throw new NotPerformedFilmorateOperationException(Constants.FRIEND_REQUEST_NOT_FOUND_EXCEPTION_INFO);
        }
    }

    public List<User> commonFriends(int userId, int otherId) {
        userService.checkUsersExistenceById(userId, otherId);
        Collection<User> userFriends = emptyIfNull(friendshipDbStorage.getUserFriends(userId));
        Collection<User> otherFriends = emptyIfNull(friendshipDbStorage.getUserFriends(otherId));
        List<User> intersection = userFriends.stream().filter(otherFriends::contains).collect(Collectors.toList());
        log.info(Constants.COMMON_FRIENDS_LOG, userId, otherId, intersection);
        return intersection;
    }
}
