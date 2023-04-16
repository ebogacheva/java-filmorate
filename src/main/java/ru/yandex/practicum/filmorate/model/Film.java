package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.yandex.practicum.filmorate.model.annotations.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Film {

    @With private
    int id;
    @NotNull(message = "Film name shouldn't be null.")
    @NotBlank(message = "Film name shouldn't be empty.")
    private String name;
    @Size(max = 200, message = "Too long description.")
    private String description;
    @ReleaseDateValidation
    private LocalDate releaseDate;
    @Positive
    private long duration;

    public Map<String, Object> toMap() { //TODO: refactor
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", this.getName());
        values.put("DESCRIPTION", this.getDescription());
        values.put("RELEASE_DATE",this.getReleaseDate());
        values.put("DURATION", this.getDuration());
        return values;
    }
}
