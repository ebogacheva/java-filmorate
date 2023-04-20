package ru.yandex.practicum.filmorate.storage.db.like;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.utils.FilmorateRowMappers;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/likes.sql"})
@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeDbStorageTest {

    @Autowired
    private final LikeStorage likeDbStorage;

    @Autowired
    private final FilmStorage filmDbStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final MpaStorage mpaStorage;

    @Autowired
    private final GenreStorage genreStorage;

    private final JdbcTemplate jdbcTemplate;

    @Test
    void likeFilm() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        User user1 = userStorage.create(getUserForTesting(1));
        likeDbStorage.likeFilm(film1.getId(), user1.getId());
        List<Integer> likes =
                jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id = ?",
                        FilmorateRowMappers::getUserId, film1.getId());
        assertEquals(1, likes.size());
    }

    @Test
    void dislikeFilm() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        User user1 = userStorage.create(getUserForTesting(1));
        User user2 = userStorage.create(getUserForTesting(1));
        likeDbStorage.likeFilm(film1.getId(), user1.getId());
        likeDbStorage.likeFilm(film1.getId(), user2.getId());
        likeDbStorage.dislikeFilm(film1.getId(), user2.getId());
        List<Integer> likes =
                jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id = ?",
                        FilmorateRowMappers::getUserId, film1.getId());
        assertEquals(1, likes.size());
    }

    @Test
    void getAllFilmLikes() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        User user1 = userStorage.create(getUserForTesting(1));
        User user2 = userStorage.create(getUserForTesting(1));
        likeDbStorage.likeFilm(film1.getId(), user1.getId());
        likeDbStorage.likeFilm(film1.getId(), user2.getId());
        List<Integer> likes = likeDbStorage.getAllFilmLikes(film1.getId());
        assertEquals(2, likes.size());
    }

    private User getUserForTesting(int index) {
        return User.builder()
                .name("name" + index)
                .email(index + "email@email.ru")
                .login("login" + index)
                .birthday(LocalDate.of(1990, 1, 10))
                .build();
    }

    private Film getFilmForTesting(int index) {
        Genre genre1 = genreStorage.getGenreById(1).orElse(null);
        Genre genre2 = genreStorage.getGenreById(2).orElse(null);
        assert genre1 != null;
        assert genre2 != null;
        return Film.builder()
                .name("name" + index)
                .description(index + "description")
                .releaseDate(LocalDate.of(1990, 1, 10))
                .duration(120)
                .mpa(mpaStorage.getMpaById(1).orElse(null))
                .genres(Set.of(genre1, genre2))
                .build();
    }
}