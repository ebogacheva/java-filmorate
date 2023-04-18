package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db.like.LikeStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeStorage likeDbStorage;

    public void likeFilm(int filmId, int userId) {
        likeDbStorage.likeFilm(filmId, userId);
    }

    public void dislikeFilm(int filmId, int userId) {
        likeDbStorage.dislikeFilm(filmId, userId);
    }

    public List<Integer> getAllFilmLikes(int filmId) {
        return likeDbStorage.getAllFilmLikes(filmId);
    }
}
