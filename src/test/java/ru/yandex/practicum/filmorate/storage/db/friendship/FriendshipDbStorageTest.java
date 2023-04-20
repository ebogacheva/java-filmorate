package ru.yandex.practicum.filmorate.storage.db.friendship;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendshipDbStorageTest {

    private static final String SQL_QUERY_DELETE_ALL_FRIENDSHIPS = "DELETE FROM friendship";

    @Autowired
    private final UserStorage userDbStorage;

    @Autowired
    private final FriendshipStorage friendshipDbStorage;

    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate.update(SQL_QUERY_DELETE_ALL_FRIENDSHIPS);
    }

    @Test
    void sendFriendRequest() {
        User user1 = userDbStorage.create(getUserForTesting(1));
        User user2 = userDbStorage.create(getUserForTesting(2));
        friendshipDbStorage.sendFriendRequest(user1.getId(), user2.getId());
        friendshipDbStorage.sendFriendRequest(user2.getId(), user1.getId());
        List<User> friends = friendshipDbStorage.getUserFriends(user1.getId());
        assertEquals(1, friends.size());
    }

    @Test
    void deleteFriend() {
        User user1 = userDbStorage.create(getUserForTesting(1));
        User user2 = userDbStorage.create(getUserForTesting(2));
        friendshipDbStorage.sendFriendRequest(user1.getId(), user2.getId());
        friendshipDbStorage.sendFriendRequest(user2.getId(), user1.getId());
        friendshipDbStorage.deleteFriend(user1.getId(), user2.getId());
        List<User> friends = friendshipDbStorage.getUserFriends(user1.getId());
        assertEquals(0, friends.size());
    }

    @Test
    void confirmRequest() {
        User user1 = userDbStorage.create(getUserForTesting(1));
        User user2 = userDbStorage.create(getUserForTesting(2));
        friendshipDbStorage.sendFriendRequest(user1.getId(), user2.getId());
        friendshipDbStorage.confirmRequest(user2.getId(), user1.getId());
        List<Integer> friendsOfUser1 = friendshipDbStorage.getConfirmedFriends(user1.getId());
        List<Integer> friendsOfUser2 = friendshipDbStorage.getConfirmedFriends(user2.getId());
        assertEquals(user2.getId(), friendsOfUser1.get(0));
        assertEquals(user1.getId(), friendsOfUser2.get(0));
    }

    @Test
    void getFriendRequests() {
        User user1 = userDbStorage.create(getUserForTesting(1));
        User user2 = userDbStorage.create(getUserForTesting(2));
        User user3 = userDbStorage.create(getUserForTesting(3));
        friendshipDbStorage.sendFriendRequest(user1.getId(), user3.getId());
        friendshipDbStorage.sendFriendRequest(user2.getId(), user3.getId());
        friendshipDbStorage.confirmRequest(user3.getId(), user2.getId());
        assertEquals(user1.getId(), friendshipDbStorage.getFriendRequests(user3.getId()).get(0));
    }

    @Test
    void getConfirmedFriends() {
        User user1 = userDbStorage.create(getUserForTesting(1));
        User user2 = userDbStorage.create(getUserForTesting(2));
        User user3 = userDbStorage.create(getUserForTesting(3));
        friendshipDbStorage.sendFriendRequest(user1.getId(), user3.getId());
        friendshipDbStorage.sendFriendRequest(user2.getId(), user3.getId());
        friendshipDbStorage.confirmRequest(user3.getId(), user2.getId());
        List<Integer> confirmedRequests = friendshipDbStorage.getConfirmedFriends(user3.getId());
        assertEquals(user2.getId(), confirmedRequests.get(0));
    }

    private User getUserForTesting(int index) {
        return User.builder()
                .name("name" + index)
                .email(index + "email@email.ru")
                .login("login" + index)
                .birthday(LocalDate.of(1990, 1, 10))
                .build();
    }
}