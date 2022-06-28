package ru.yandex.practicum.filmorate.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Mpa {
    int id;
    String name;

    public Mpa(int id) {
        this.id = id;
    }
}
