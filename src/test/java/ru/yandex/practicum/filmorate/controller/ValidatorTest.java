package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void validateFilmWithValidDataOk() {
        Film film = TestDataProvider.getFilmWithValidData();
        boolean actual = Validator.isValidated(film);
        assertTrue(actual);
    }

    @Test
    void validateFilmWithBlankNameException() {
        Film film = TestDataProvider.getFilmWithEmptyName();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }

    @Test
    void validateFilmWithNullNameException() {
        Film film = TestDataProvider.getFilmWithNullName();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }

    @Test
    void validateFilmWithTooLongDescriptionException() {
        Film film = TestDataProvider.getFilmWithTooLongDescription();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }

    @Test
    void validateFilmReleaseInvalidDateException() {
        Film film = TestDataProvider.getFilmWithInvalidReleaseDate();
        assertThrows(ValidationException.class, () -> Validator.isValidated(film));
    }

    @Test
    void validateFilmReleaseExactStartFilmHistoryDateOk() {
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

    @Test
    void validateUserWithInvalidLoginSpacesException() {
        User user = TestDataProvider.getUserWithInvalidLoginSpaces();
        assertThrows(ValidationException.class, () -> Validator.isValidated(user));
    }

    @Test
    void validateUserWithNullLoginException() {
        User user = TestDataProvider.getUserWithNullLoginSpaces();
        assertThrows(ValidationException.class, () -> Validator.isValidated(user));
    }

    @Test
    void validateUserWithEmptyLoginException() {
        User user = TestDataProvider.getUserWithEmptyLoginSpaces();
        assertThrows(ValidationException.class, () -> Validator.isValidated(user));
    }

    @Test
    void validateUserWithFutureBirthdayException() {
        User user = TestDataProvider.getUserWithFutureBirthday();
        assertThrows(ValidationException.class, () -> Validator.isValidated(user));
    }
}