package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    @Autowired
    private final FilmStorage filmDbStorage;

    @Autowired
    private final MpaStorage mpaStorage;

    @Autowired
    private final GenreStorage genreStorage;

    @BeforeEach
    void beforeEach() {
        List<Film> films = filmDbStorage.findAll();
        films.forEach(film -> filmDbStorage.delete(film.getId()));
    }

    @Test
    void create() {
        Film film = getFilmForTesting(1);
        Film actual = filmDbStorage.create(film);
        Film expected = film.withId(actual.getId());
        compareFilmFields(expected, actual);

    }

    @Test
    void getById() {
        Film film = getFilmForTesting(1);
        Film expected = filmDbStorage.create(film);
        Film actual = filmDbStorage.getById(expected.getId());
        compareFilmFields(expected, actual);
    }

    @Test
    void findAll() {
        filmDbStorage.create(getFilmForTesting(1));
        filmDbStorage.create(getFilmForTesting(2));
        List<Film> actual = filmDbStorage.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void update() {
        Film created = filmDbStorage.create(getFilmForTesting(1));
        created.setDescription("new description");
        filmDbStorage.update(created);
        Film actual = filmDbStorage.getById(created.getId());
        assertThat(actual).hasFieldOrPropertyWithValue("description", "new description");
    }

    @Test
    void delete() {
        Film created = filmDbStorage.create(getFilmForTesting(1));
        filmDbStorage.delete(created.getId());
        assertThrows(DataAccessException.class, () -> filmDbStorage.getById(created.getId()));
    }

    private void compareFilmFields(Film expected, Film actual) {
        assertThat(actual).hasFieldOrPropertyWithValue("name", expected.getName());
        assertThat(actual).hasFieldOrPropertyWithValue("description", expected.getDescription());
        assertThat(actual).hasFieldOrPropertyWithValue("releaseDate", expected.getReleaseDate());
        assertThat(actual).hasFieldOrPropertyWithValue("duration", expected.getDuration());
        assertThat(actual).hasFieldOrPropertyWithValue("mpa", expected.getMpa());
        assertThat(actual).hasFieldOrPropertyWithValue("genres", expected.getGenres());
        assertThat(actual).hasFieldOrPropertyWithValue("id", expected.getId());
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