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
    public User put(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable int id) {
        return userService.getById(id);
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

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable int id,
                                    @PathVariable int otherId) {
        return userService.commonFriends(id, otherId);
    }
}
