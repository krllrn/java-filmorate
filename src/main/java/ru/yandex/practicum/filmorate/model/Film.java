package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;

    @Size(max = 200)
    @NotNull
    @NotBlank
    @NotEmpty
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;
}
