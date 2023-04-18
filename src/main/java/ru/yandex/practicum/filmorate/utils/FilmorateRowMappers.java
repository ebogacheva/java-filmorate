package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class FilmorateRowMappers {

    public static Film getFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .build();
    }

    public static User getUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .login(resultSet.getString("LOGIN"))
                .email(resultSet.getString("EMAIL"))
                .birthday(Objects.requireNonNull(resultSet.getDate("BIRTHDAY").toLocalDate()))
                .build();
    }

    public static int getIdForUser(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("ID");
    }

    public static int getUserId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("USER_ID");
    }

    public static Mpa getMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }

    public static Genre getGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }

}
