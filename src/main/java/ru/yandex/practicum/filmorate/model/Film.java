package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {

    @With
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
}
