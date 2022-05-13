package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull
    @NotBlank
    @NotEmpty
    private final String name;

    @Size(max = 200)
    @NotNull
    @NotBlank
    @NotEmpty
    private final String description;

    private final LocalDate releaseDate;

    @Positive
    private final int duration;
}
