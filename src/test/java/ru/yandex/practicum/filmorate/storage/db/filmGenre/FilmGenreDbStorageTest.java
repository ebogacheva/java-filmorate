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
import java.util.List;
import java.util.Set;

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
        Film updated = filmDbStorage.getById(film1.getId());
        assertEquals(2, film1.getGenres().size());
        assertEquals(3, updated.getGenres().size());
    }

    @Test
    void deleteFilmGenre() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        filmGenreDbStorage.deleteFilmGenre(film1.getId(), 2);
        Film updated = filmDbStorage.getById(film1.getId());
        assertEquals(1, updated.getGenres().size());
    }

    @Test
    void getAllFilmGenresById() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        List<Genre> genres = filmGenreDbStorage.getAllFilmGenresById(film1.getId());
        assertEquals(2, genres.size());
    }

    private Film getFilmForTesting(int index) {
        return Film.builder()
                .name("name" + index)
                .description(index + "description")
                .releaseDate(LocalDate.of(1990, 1, 10))
                .duration(120)
                .mpa(mpaStorage.getMpaById(1))
                .genres(Set.of(genreStorage.getGenreById(1), genreStorage.getGenreById(2)))
                .build();
    }
}