package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    @Autowired
    private final UserStorage userDbStorage;

    @BeforeEach
    void beforeEach() {
        List<User> users = userDbStorage.findAll();
        users.forEach(user -> userDbStorage.delete(user.getId()));
    }

    @Test
    void createUser() {
        User user = getUserForTesting(1);
        User actual = userDbStorage.create(user);
        User expected = user.withId(actual.getId());
        compareUserFields(expected, actual);
    }

    @Test
    void getById() {
        User user = getUserForTesting(1);
        User expected = userDbStorage.create(user);
        Optional<User> actual = userDbStorage.getById(expected.getId());
        assertTrue(actual.isPresent());
        compareUserFields(expected, actual.get());
    }

    @Test
    void findAll() {
        userDbStorage.create(getUserForTesting(1));
        userDbStorage.create(getUserForTesting(2));
        List<User> actual = userDbStorage.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void update() {
        User created = userDbStorage.create(getUserForTesting(1));
        created.setEmail("new@email.ru");
        userDbStorage.update(created);
        Optional<User> actual = userDbStorage.getById(created.getId());
        assertTrue(actual.isPresent());
        assertThat(actual).get().hasFieldOrPropertyWithValue("email", "new@email.ru");
    }

    @Test
    void delete() {
        User created = userDbStorage.create(getUserForTesting(1));
        assertTrue(userDbStorage.delete(created.getId()));
        assertThat(userDbStorage.getById(created.getId())).isEmpty();
    }

    private void compareUserFields(User expected, User actual) {
        assertThat(actual).hasFieldOrPropertyWithValue("email", expected.getEmail());
        assertThat(actual).hasFieldOrPropertyWithValue("name", expected.getName());
        assertThat(actual).hasFieldOrPropertyWithValue("login", expected.getLogin());
        assertThat(actual).hasFieldOrPropertyWithValue("birthday", expected.getBirthday());
        assertThat(actual).hasFieldOrPropertyWithValue("id", expected.getId());
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