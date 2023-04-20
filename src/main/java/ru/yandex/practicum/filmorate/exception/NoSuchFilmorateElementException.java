package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchFilmorateElementException extends RuntimeException {
    public NoSuchFilmorateElementException(String s) {
        super(s);
    }

}
