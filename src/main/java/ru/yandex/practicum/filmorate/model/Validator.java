package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class Validator {

    private static final String VALIDATION_USER_ERROR_BASIC_MESSAGE = "Валидация пользователя не пройдена: ";
    private static final String VALIDATION_FILM_ERROR_BASIC_MESSAGE = "Валидация фильма не пройдена: ";
    private static final String BIRTHDAY_INVALID_MESSAGE  = "Дата рождения не может быть в будущем.";
    private static final String LOGIN_INVALID_MESSAGE = "Логин не может быть пустым или содержать пробелы.";
    private static final String EMAIL_INVALID_MESSAGE = "Email не может быть пустым и должен содержать символ @.";
    private static final String INVALID_DURATION_MESSAGE = "Продолжительность фильма должна быть положительной.";
    private static final String RELEASE_INVALID_MESSAGE = "Дата релиза не может быть раньше 28.12.1895.";
    private static final String DESCRIPTION_INVALID_MESSAGE = "Описание фильма должно быть <= 200 символов.";
    private static final String FILM_NAME_INVALID_MESSAGE = "Название фильма не может быть пустым.";

    public static void isValidated(Film film) {
        LocalDate filmHistoryStart = LocalDate.of(1895, 12, 28);
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            log.warn(VALIDATION_FILM_ERROR_BASIC_MESSAGE + FILM_NAME_INVALID_MESSAGE);
            throw new ValidationException(FILM_NAME_INVALID_MESSAGE);
        }
        if (film.getDescription().length() > 200) {
            log.warn(VALIDATION_FILM_ERROR_BASIC_MESSAGE + DESCRIPTION_INVALID_MESSAGE);
            throw new ValidationException(DESCRIPTION_INVALID_MESSAGE);
        }
        if (film.getReleaseDate().isBefore(filmHistoryStart)) {
            log.warn(VALIDATION_FILM_ERROR_BASIC_MESSAGE + RELEASE_INVALID_MESSAGE);
            throw new ValidationException(RELEASE_INVALID_MESSAGE);
        }
        if (film.getDuration() <= 0) {
            log.warn(VALIDATION_FILM_ERROR_BASIC_MESSAGE + INVALID_DURATION_MESSAGE);
            throw new ValidationException(INVALID_DURATION_MESSAGE);
        }
    }

    public static void isValidated(User user) {
        if (Objects.isNull(user.getEmail()) || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn(VALIDATION_USER_ERROR_BASIC_MESSAGE + EMAIL_INVALID_MESSAGE);
            throw new ValidationException(EMAIL_INVALID_MESSAGE);
        }
        if (Objects.isNull(user.getLogin()) || user.getLogin().isBlank() || user.getLogin().chars().anyMatch(Character::isWhitespace)) {
            log.warn(VALIDATION_USER_ERROR_BASIC_MESSAGE + LOGIN_INVALID_MESSAGE);
            throw new ValidationException(LOGIN_INVALID_MESSAGE);
        }
        if (Objects.isNull(user.getName()) || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn(VALIDATION_USER_ERROR_BASIC_MESSAGE + BIRTHDAY_INVALID_MESSAGE);
            throw new ValidationException(BIRTHDAY_INVALID_MESSAGE);
        }
    }

}
