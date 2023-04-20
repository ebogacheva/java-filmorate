package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = "name")
public class Genre implements Comparable<Genre> {

    private int id;
    private String name;

    @Override
    public int compareTo(Genre genre) {
        return Integer.compare(this.id, genre.id);
    }
}