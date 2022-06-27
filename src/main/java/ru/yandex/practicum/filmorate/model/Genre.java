package ru.yandex.practicum.filmorate.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Genre {
    int id;
    String name;

    public Genre(int id) {
        this.id = id;
    }
}
