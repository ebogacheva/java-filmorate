package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Email;

import lombok.*;
import ru.yandex.practicum.filmorate.model.annotations.UserLoginValidation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @With
    private int id;
    @NotNull(message = "User email shouldn't be null.")
    @NotBlank(message = "User email shouldn't be empty.")
    @Email
    private String email;
    @NotNull(message = "User login shouldn't be null.")
    @NotBlank(message = "User login shouldn't be empty.")
    @UserLoginValidation
    private String login;
    @With
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public Map<String, Object> toMap() { //TODO: refactor
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", this.getEmail());
        values.put("LOGIN", this.getLogin());
        values.put("NAME",this.getName());
        values.put("BIRTHDAY", this.getBirthday());
        return values;
    }
}
