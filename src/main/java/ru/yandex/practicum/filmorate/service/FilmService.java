package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    @Autowired
    private FilmStorage filmDbStorage;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MpaService mpaService;

    @Autowired
    private LikeService likeService;

    public Film create(Film film) {
        log.info("Добавлен фильм {}", film);
        return filmDbStorage.create(film);
    }

    public Film getById(int filmId) {
        Film film;
        try {
            film = filmDbStorage.getById(filmId);
        } catch (DataAccessException ex) {
            log.info("Фильма не найден: {}", filmId);
            throw new NoSuchFilmException(Constants.FILM_NOT_FOUND_INFO);
        }
        return film;
    }

    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

    public List<User> getAllFilmLikes(int filmId) {
        checkFilmsExistenceById(filmId);
        List<Integer> likes = likeService.getAllFilmLikes(filmId);
        return userService.getUsersList(likes);
    }

    public Film update(Film film) {
        checkFilmsExistenceById(film.getId());
        log.info("Обновлена информация о фильме {}", film);
        return filmDbStorage.update(film);
    }

    public void delete(int filmId) {
        try {
            if (filmDbStorage.delete(filmId)) {
                log.info("Удален фильм: {}", filmId);
            }
        } catch (DataAccessException ex) {
            log.info("Фильм не найден: {}", filmId);
            throw new NoSuchUserException(Constants.FILM_NOT_FOUND_INFO);
        }
    }

    public Film like(int filmId, int userId) {
        checkFilmsExistenceById(filmId);
        userService.checkUsersExistenceById(userId);
        likeService.likeFilm(filmId, userId);
        return getById(filmId);
    }

    public Film dislike(int filmId, int userId) {
        checkFilmsExistenceById(filmId);
        userService.checkUsersExistenceById(userId);
        likeService.dislikeFilm(filmId, userId);
        return getById(filmId);
    }

    public List<Film> popular(int count) {
        return filmDbStorage.findAll()
                .stream()
                .sorted((film, film2) ->
                        -(likeService.getAllFilmLikes(film.getId()).size()
                                - likeService.getAllFilmLikes(film2.getId()).size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void checkFilmsExistenceById(int...filmIds) {
        for (int id : filmIds) {
            getById(id);
        }
    }
}
