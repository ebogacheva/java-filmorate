package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchFriendRequestException extends RuntimeException {
    public NoSuchFriendRequestException(String s) {
        super((s));
    }
}

