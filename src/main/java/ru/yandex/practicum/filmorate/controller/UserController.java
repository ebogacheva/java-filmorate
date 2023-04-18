package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = "/users")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable int id) {
        return userService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        userService.sendFriendRequest(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> friends(@PathVariable int id) {
        return userService.friends(id);
    }

    @GetMapping(value = "/{id}/requests")
    public List<User> requests(@PathVariable int id) {
        return userService.getFriendsRequests(id);
    }

    @GetMapping(value = "/{id}/friends/confirmed")
    public List<User> confirmedFriends(@PathVariable int id) {
        return userService.confirmedFriends(id);
    }

    @PutMapping(value = "/{id}/requests/{otherId}/confirm")
    public void confirmRequest(@PathVariable int id,
                               @PathVariable int otherId) {
        userService.confirmFriendRequest(id, otherId);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable int id,
                                    @PathVariable int otherId) {
        return userService.commonFriends(id, otherId);
    }
}
