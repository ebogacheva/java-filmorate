package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void validateFilmWithValidData() {
        Film film = TestDataProvider.getFilmWithValidData();
        boolean actual = Validator.isValidated(film);
        assertTrue(actual);
    }

    @Test
    void validateFilmWithBlankName() {
        Film film = TestDataProvider.getFilmWithInvalidName();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }

    @Test
    void validateFilmWithTooLongDescription() {
        Film film = TestDataProvider.getFilmWithTooLongDescription();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }

    @Test
    void validateFilmReleaseInvalidDate() {
        Film film = TestDataProvider.getFilmWithInvalidReleaseDate();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }

    @Test
    void validateFilmReleaseExactStartFilmHistoryDate() {
        Film film = TestDataProvider.getFilmWithExactStartFilmHistoryDate();
        boolean actual = Validator.isValidated(film);
        assertTrue(actual);
    }

    @Test
    void validateFilmWithInvalidDuration() {
        Film film = TestDataProvider.getFilmWithInvalidDuration();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }
    @Test
    void validatedUserWithValidData() {
        User user = TestDataProvider.getUserWithValidData();
        boolean actual = Validator.isValidated(user);
        assertTrue(actual);
    }

    @Test
    void validateUserWithEmptyEmail() {
        User user = TestDataProvider.getUserWithEmptyEmail();
        assertThrows(ValidationException.class, () -> Validator.isValidated(user));
    }

    @Test
    void validateUserWithNullEmail() {
        User user = TestDataProvider.getUserWithNullEmail();
        assertThrows(ValidationException.class, () -> Validator.isValidated(user));
    }

    @Test
    void validateUserWithInvalidEmail() {
        User user = TestDataProvider.getUserWithInvalidEmail();
        assertThrows(ValidationException.class, () -> Validator.isValidated(user));
    }
}