package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private static final AtomicInteger ID_PROVIDER = new AtomicInteger(0);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        int id = ID_PROVIDER.incrementAndGet();
        Film filmCreated = film.withId(id);
        log.debug("Добавлен фильм: {}", filmCreated);
        films.put(id, filmCreated);
        return filmCreated;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film put(Film film) {
        log.debug("Обновлены данные фильма: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }
}
