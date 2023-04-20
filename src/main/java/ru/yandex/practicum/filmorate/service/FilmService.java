package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.exception.NotPerformedOperationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    @Autowired
    private FilmStorage filmDbStorage;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeDbStorage likeDbStorage;

    public Film create(Film film) {
        Film createdFilm = filmDbStorage.create(film);
        log.info(Constants.FILM_ADDED_LOG, film);
        return createdFilm;
    }

    public Film getById(int filmId) {
        Optional<Film> film = filmDbStorage.getById(filmId);
        if (film.isPresent()) {
            log.info(Constants.GOT_FILM_BY_ID, filmId);
            return film.get();
        } else {
            log.info(Constants.FILM_NOT_FOUND_LOG, filmId);
            throw new NoSuchElementException(Constants.FILM_NOT_FOUND_EXCEPTION_INFO);
        }
    }

    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

    public List<User> getAllFilmLikes(int filmId) {
        checkFilmsExistenceById(filmId);
        List<Integer> likes = likeDbStorage.getAllFilmLikes(filmId);
        log.info(Constants.USERS_LIKES_LOG, filmId, likes.size());
        return userService.getUsersList(likes);
    }

    public Film update(Film film) {
        checkFilmsExistenceById(film.getId());
        Optional<Film> updatedFilm = filmDbStorage.update(film);
        if (updatedFilm.isPresent()) {
            log.info(Constants.UPDATED_FILM_LOG, film.getId());
            return updatedFilm.get();
        } else throw new NotPerformedOperationException(Constants.UPDATE_NOT_PERFORMED_EXCEPTION_INFO);
    }

    public void delete(int filmId) {
        if(!filmDbStorage.delete(filmId)) {
            throw new NotPerformedOperationException(Constants.DELETE_NOT_PERFORMED_EXCEPTION_INFO);
        }
        log.info(Constants.DELETED_FILM_LOG, filmId);
    }

    public Film like(int filmId, int userId) {
        checkFilmsExistenceById(filmId);
        if (likeDbStorage.likeFilm(filmId, userId)) {
            log.info(Constants.USER_LIKE_FILM_LOG, userId, filmId);
            return getById(filmId);
        } else throw new NotPerformedOperationException(Constants.LIKE_NOT_PERFORMED_OPERATION_INFO);
    }

    public Film dislike(int filmId, int userId) {
        checkFilmsExistenceById(filmId);
        userService.checkUsersExistenceById(userId);
        if (likeDbStorage.dislikeFilm(filmId, userId)) {
            log.info(Constants.USER_DISLIKE_FILM_LOG, userId, filmId);
            return getById(filmId);
        } else throw new NotPerformedOperationException(Constants.DISLIKE_NOT_PERFORMED_OPERATION_INFO);
    }


    public List<Film> popular(int count) {
        return filmDbStorage.findAll()
                .stream()
                .sorted((film, film2) ->
                        -(getAllFilmLikes(film.getId()).size()
                                - getAllFilmLikes(film2.getId()).size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void checkFilmsExistenceById(int...filmIds) {
        for (int id : filmIds) {
            getById(id);
        }
    }
}
