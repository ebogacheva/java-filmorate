package ru.yandex.practicum.filmorate.storage.db.like;


import java.util.List;

public interface LikeStorage {

    void likeFilm(int filmId, int userId);
    void dislikeFilm(int filmId, int userId);
    List<Integer> getAllFilmLikes(int filmId);
}
