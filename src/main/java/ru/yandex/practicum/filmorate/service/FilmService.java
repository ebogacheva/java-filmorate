package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    @Autowired
    private FilmStorage inMemoryFilmStorage;

    @Autowired
    private UserService userService;

    public Film create(Film film) {
        log.info("Добавлен фильм {}", film);
        return inMemoryFilmStorage.create(film);
    }

    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film put(Film film) {
        if (Objects.isNull(inMemoryFilmStorage.getById(film.getId()))) {
            throw new NoSuchFilmException(Constants.FILM_NOT_FOUND_INFO);
        }
        log.info("Обновлена информация о фильме {}", film);
        return inMemoryFilmStorage.put(film);
    }

    public Film like(int id, int userId) {
        checkFilmsExistenceById(id);
        userService.checkUsersExistenceById(userId);
        inMemoryFilmStorage.getById(id).getLikes().add(userId);
        log.info("Фильм {} нравится {} пользователям", id, userId);
        return inMemoryFilmStorage.getById(id);
    }

    public Film dislike(int id, int userId) {
        checkFilmsExistenceById(id);
        userService.checkUsersExistenceById(userId);
        inMemoryFilmStorage.getById(id).getLikes().remove(userId);
        log.info("Фильм {} больше не нравится пользователю {}", id, userId);
        return inMemoryFilmStorage.getById(id);
    }

    public List<Film> popular(int count) {
        return inMemoryFilmStorage.findAll()
                .stream()
                .sorted((film, film2) -> -(film.getLikes().size() - film2.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getById(int filmId) {
        checkFilmsExistenceById(filmId);
        return inMemoryFilmStorage.getById(filmId);
    }

    private void checkFilmsExistenceById(int...filmIds) {
        for (int id : filmIds) {
            if (Objects.isNull(inMemoryFilmStorage.getById(id))) {
                throw new NoSuchFilmException(Constants.FILM_NOT_FOUND_INFO);
            }
        }
    }
}
