package ru.yandex.practicum.filmorate.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Genre implements Comparable<Genre> {
    int id;
    String name;

    public Genre(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Genre o) {
        return id - o.getId();
    }
}
