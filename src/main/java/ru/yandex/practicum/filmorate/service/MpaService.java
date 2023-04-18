package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.filmMpa.FilmMpaStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.utils.Constants;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaDbStorage;

    private final FilmMpaStorage filmMpaDbStorage;

    public Mpa getMpaById(int id) {
        Mpa mpa;
        try {
            mpa = mpaDbStorage.getMpaById(id);
        } catch (DataAccessException ex) {
            throw new NoSuchMpaException(Constants.RATING_MPA_NOT_FOUND_EXCEPTION_INFO);
        }
        return mpa;
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAll();
    }

}
