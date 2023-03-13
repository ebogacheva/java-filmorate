package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Email;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.yandex.practicum.filmorate.model.annotations.UserLoginValidation;

import java.time.LocalDate;

@Data
@Builder
public class User {

    @With private int id;
    @NotNull @NotBlank @Email private String email;
    @NotNull @NotBlank @UserLoginValidation private String login;
    @With private String name;
    @PastOrPresent private LocalDate birthday;
}
