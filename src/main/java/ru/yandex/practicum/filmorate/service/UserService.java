package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmorateElementException;
import ru.yandex.practicum.filmorate.exception.NotPerformedFilmorateOperationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserStorage userDbStorage;

    public User create(User user) {
        String name = getCorrectName(user);
        User userWithCorrectName = user.withName(name);
        return userDbStorage.create(userWithCorrectName);
    }

    public User getById(int userId) {
        Optional<User> user = userDbStorage.getById(userId);
        if (user.isPresent()) {
            log.info(Constants.GOT_USER_BY_ID_LOG, userId);
            return user.get();
        } else {
            log.warn(Constants.USER_NOT_FOUND_LOG, userId);
            throw new NoSuchFilmorateElementException(Constants.USER_NOT_FOUND_EXCEPTION_INFO);
        }
    }

    public List<User> findAll() {
        return userDbStorage.findAll();
    }

    public User update(User user) {
        checkUsersExistenceById(user.getId());
        String name = getCorrectName(user);
        User userWithCorrectName = user.withName(name);
        Optional<User> updated = userDbStorage.update(userWithCorrectName);
        if (updated.isPresent()) {
            log.info(Constants.UPDATED_USER_LOG, updated.get().getId());
            return updated.get();
        } else {
            throw new NotPerformedFilmorateOperationException(Constants.UPDATE_NOT_PERFORMED_EXCEPTION_INFO);
        }
    }

    public void delete(int id) {
        if (!userDbStorage.delete(id)) {
            throw new NotPerformedFilmorateOperationException(Constants.DELETE_NOT_PERFORMED_EXCEPTION_INFO);
        }
        log.info(Constants.USER_DELETED_LOG, id);
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
