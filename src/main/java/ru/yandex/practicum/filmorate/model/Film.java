package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> likes = new HashSet<>();

    int likesValue = 0;

    public void addLikes(User user) {
        likes.add(user);
        likesValue++;
    }

    public void removeLikes(User user) {
        likes.remove(user);
        likesValue--;
    }
}
