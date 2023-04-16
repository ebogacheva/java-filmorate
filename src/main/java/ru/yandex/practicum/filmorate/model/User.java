package ru.yandex.practicum.filmorate.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Email;

import lombok.*;
import ru.yandex.practicum.filmorate.model.annotations.UserLoginValidation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    //private Set<Integer> friends = new HashSet<>();
    //private Set<Integer> sentRequests = new HashSet<>();
   // private Set<Integer> receivedRequests = new HashSet<>();

//    public User(int id, @Valid String email, @Valid String login, String name, LocalDate birthday) {
//        this.id = id;
//        this.email = email;
//        this.login = login;
//        this.name = name;
//        this.birthday = birthday;
//        //this.friends = new HashSet<>();
//        //this.sentRequests = new HashSet<>();
//       // this.receivedRequests = new HashSet<>();
//    }

    public Map<String, Object> toMap() { //TODO: refactor
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", this.getEmail());
        values.put("LOGIN", this.getLogin());
        values.put("NAME",this.getName());
        values.put("BIRTHDAY", this.getBirthday());
        return values;
    }
}
