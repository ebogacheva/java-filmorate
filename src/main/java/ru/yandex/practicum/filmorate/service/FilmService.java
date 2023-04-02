package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    @Autowired
    FilmStorage filmStorage;

    @Autowired
    UserService userService;

    public Film create (Film film) {
        return filmStorage.create(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film put(Film film) {
        if (Objects.isNull(filmStorage.getById(film.getId()))) {
            throw new NoSuchFilmException("Фильм не найден - обновление невозможно.");
        }
        return filmStorage.put(film);
    }

    public Film like(int id, int userId) {
        checkFilmsExistenceById(id);
        userService.checkUsersExistenceById(userId);
        filmStorage.getById(id).getLikes().add(userId);
        return filmStorage.getById(id);
    }

    public Film dislike(int id, int userId) {
        checkFilmsExistenceById(id);
        userService.checkUsersExistenceById(userId);
        filmStorage.getById(id).getLikes().remove(userId);
        return filmStorage.getById(id);
    }

    public List<Film> popular(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkFilmsExistenceById(int...filmIds) {
        for (int id : filmIds) {
            if (Objects.isNull(filmStorage.getById(id))) {
                throw new NoSuchFilmException(Constants.FILM_NOT_FOUND_INFO);
            }
        }
    }

}
