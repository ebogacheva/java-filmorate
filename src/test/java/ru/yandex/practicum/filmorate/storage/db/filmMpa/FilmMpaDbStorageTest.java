package ru.yandex.practicum.filmorate.storage.db.filmMpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.time.LocalDate;
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
        Mpa mpa = filmMpaDbStorage.getFilmMpaById(film1.getId());
        assertEquals(3, mpa.getId());
    }

    @Test
    void deleteFilmMap() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        filmMpaDbStorage.deleteFilmMap(film1.getId());
        Mpa mpa = filmMpaDbStorage.getFilmMpaById(film1.getId());
        assertNull(mpa);
    }

    @Test
    void getFilmMpaById() {
        Film film1 = filmDbStorage.create(getFilmForTesting(1));
        Mpa actual = filmMpaDbStorage.getFilmMpaById(film1.getId());
        assertThat(actual).hasFieldOrPropertyWithValue("name", "G");
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