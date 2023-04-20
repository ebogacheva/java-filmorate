package ru.yandex.practicum.filmorate.storage.db.filmMpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmMpaDbStorageTest {

    @Autowired
    private final FilmMpaDbStorage filmMpaDbStorage;

    @Autowired
    private final FilmDbStorage filmDbStorage;

    @Autowired
    private final MpaStorage mpaStorage;

    @Autowired
    private final GenreStorage genreStorage;

    @Test
    void addFilmMpa() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        filmMpaDbStorage.setFilmMpa(film1.getId(), 3);
        Optional<Mpa> mpa = filmMpaDbStorage.getFilmMpaById(film1.getId());
        assertTrue(mpa.isPresent());
        assertEquals(3, mpa.get().getId());
    }

    @Test
    void deleteFilmMpa() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        filmMpaDbStorage.deleteFilmMap(film1.getId());
        Optional<Mpa> mpa = filmMpaDbStorage.getFilmMpaById(film1.getId());
        assertTrue(mpa.isEmpty());
    }

    @Test
    void getFilmMpaById() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        Optional<Mpa> mpa = filmMpaDbStorage.getFilmMpaById(film1.getId());
        assertTrue(mpa.isPresent());
        assertThat(mpa).get().hasFieldOrPropertyWithValue("name", "G");
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