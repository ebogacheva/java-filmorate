package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class, MethodArgumentNotValidException.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationHandle(final MethodArgumentNotValidException e) {
        return Map.of(Constants.ERROR_MESSAGE, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchUser(final NoSuchUserException e) {
        return Map.of(Constants.ERROR_MESSAGE, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchFilm(final NoSuchFilmException e) {
        return Map.of(Constants.ERROR_MESSAGE, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchFilm(final NoSuchGenreException e) {
        return Map.of(Constants.ERROR_MESSAGE, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchMpa(final NoSuchMpaException e) {
        return Map.of(Constants.ERROR_MESSAGE, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationFailed(final ValidationException e) {
        return Map.of(Constants.ERROR_MESSAGE, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(final RuntimeException e) {
        return Map.of(Constants.ERROR_MESSAGE, e.getMessage());
    }



}
