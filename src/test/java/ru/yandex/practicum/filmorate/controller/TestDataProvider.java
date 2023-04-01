package ru.yandex.practicum.filmorate.controller;

import org.apache.logging.log4j.util.Strings;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class TestDataProvider {

    public static Film getFilmWithValidData() {
        return Film.builder()
                .id(0)
                .name("film")
                .description("description")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 10, 10))
                .build();
    }

    public static User getUserWithValidData() {
        return User.builder()
                .id(0)
                .email("name@gmail.com")
                .login("name")
                .name("nickname")
                .birthday(LocalDate.of(1999, 12, 12))
                .build();
    }

    public static Film getFilmWithEmptyName() {
        Film film = getFilmWithValidData();
        film.setName(" ");
        return film;
    }

    public static Film getFilmWithNullName() {
        Film film = getFilmWithValidData();
        film.setName(null);
        return film;
    }

    public static Film getFilmWithTooLongDescription() {
        Film film = getFilmWithValidData();
        film.setDescription(Strings.repeat("*", 201));
        return film;
    }

    public static Film getFilmWithInvalidReleaseDate() {
        Film film = getFilmWithValidData();
        film.setReleaseDate(LocalDate.of(1700, 1, 1));
        return film;
    }

    public static Film getFilmWithExactStartFilmHistoryDate() {
        Film film = getFilmWithValidData();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        return film;
    }

    public static Film getFilmWithInvalidDuration() {
        Film film = getFilmWithValidData();
        film.setDuration(0);
        return film;
    }

    public static User getUserWithEmptyEmail() {
        User user = getUserWithValidData();
        user.setEmail("");
        return user;
    }

    public static User getUserWithNullEmail() {
        User user = getUserWithValidData();
        user.setEmail(null);
        return user;
    }

    public static User getUserWithInvalidEmail() {
        User user = getUserWithValidData();
        user.setEmail("namegmail.com");
        return user;
    }

    public static User getUserWithInvalidLoginSpaces() {
        User user = getUserWithValidData();
        user.setLogin("login login");
        return user;
    }


    public static User getUserWithNullLoginSpaces() {
        User user = getUserWithValidData();
        user.setLogin(null);
        return user;
    }

    public static User getUserWithEmptyLoginSpaces() {
        User user = getUserWithValidData();
        user.setLogin("");
        return user;
    }

    public static User getUserWithFutureBirthday() {
        User user = getUserWithValidData();
        user.setBirthday(LocalDate.of(9999, 12, 12));
        return user;
    }


}
