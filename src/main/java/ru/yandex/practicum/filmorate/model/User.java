package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {

    @With
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
