package ru.yandex.practicum.filmorate.storage.db.filmGenre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreDbStorageTest {

    @Autowired
    private final FilmGenreStorage filmGenreDbStorage;

    @Autowired
    private final FilmDbStorage filmDbStorage;

    @Autowired
    private final MpaStorage mpaStorage;

    @Autowired
    private final GenreStorage genreStorage;

    @Test
    void addFilmGenre() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        filmGenreDbStorage.addFilmGenre(film1.getId(), 3);
        Optional<Film> updated = filmDbStorage.getById(film1.getId());
        if (updated.isPresent()) {
            assertEquals(2, film1.getGenres().size());
            assertEquals(3, updated.get().getGenres().size());
        }
    }

    @Test
    void deleteFilmGenre() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        filmGenreDbStorage.deleteFilmGenre(film1.getId(), 2);
        Optional<Film> updated = filmDbStorage.getById(film1.getId());
        updated.ifPresent(film -> assertEquals(1, film.getGenres().size()));
    }

    @Test
    void getAllFilmGenresById() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        List<Genre> genres = filmGenreDbStorage.getAllFilmGenresById(film1.getId());
        assertEquals(2, genres.size());
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