package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Film {
    private int id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Mpa mpa;

    private Set<Genre> genres;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> likes = new HashSet<>();

    public Film (String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film (int id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void addLikes(User user) {
        likes.add(user);
    }

    public void removeLikes(User user) {
        likes.remove(user);
    }

}
