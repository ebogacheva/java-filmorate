package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmorateElementException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.filmMpa.FilmMpaStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaDbStorage;
    private final FilmMpaStorage filmMpaStorage;

    public Mpa getMpaById(int id) {
        Optional<Mpa> mpa = mpaDbStorage.getMpaById(id);
        if (mpa.isPresent()) {
            return mpa.get();
        } else throw new NoSuchFilmorateElementException(Constants.RATING_MPA_NOT_FOUND_EXCEPTION_INFO);
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAll();
    }

    public void setFilmMpa(int filmId, int mpaId) {
        filmMpaStorage.setFilmMpa(filmId, mpaId);
    }

    public void deleteFilmMap(int filmId) {
        filmMpaStorage.deleteFilmMap(filmId);
    }

    public Mpa getFilmMpaById(int filmId) {
        Optional<Mpa> mpa = filmMpaStorage.getFilmMpaById(filmId);
        if (mpa.isPresent()) {
            return mpa.get();
        } else throw new NoSuchFilmorateElementException(Constants.RATING_MPA_NOT_FOUND_EXCEPTION_INFO);
    }
}
