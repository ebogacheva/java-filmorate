package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDate;

@Data
@Builder
public class User {

    @With
    private int id;
    @Email
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
