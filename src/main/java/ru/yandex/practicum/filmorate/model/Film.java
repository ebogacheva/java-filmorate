package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.yandex.practicum.filmorate.model.annotations.ReleaseDateValidation;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    @With private int id;
    @NotNull(message = "Film name shouldn't be null.")
    @NotBlank(message = "Film name shouldn't be empty.") private String name;
    @Size(max = 200, message = "Too long description.") private String description;
    @ReleaseDateValidation private LocalDate releaseDate;
    @Positive private long duration;
}
