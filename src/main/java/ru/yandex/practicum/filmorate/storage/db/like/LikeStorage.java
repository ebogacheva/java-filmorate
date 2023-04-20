package ru.yandex.practicum.filmorate.storage.db.like;


import java.util.List;

public interface LikeStorage {
    boolean likeFilm(int filmId, int userId);

    boolean dislikeFilm(int filmId, int userId);

    List<Integer> getAllFilmLikes(int filmId);

}
