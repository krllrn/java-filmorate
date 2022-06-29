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
public class User {
    private int id;

    @NotNull
    @NotBlank
    @NotEmpty
    @Email
    private String email;

    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^\\S*$")
    private String login;

    @Past
    private LocalDate birthday;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> friends = new HashSet<>();

    public User (String email, String name, String login, LocalDate birthday) {
        this.email = email;
        this.name = name;
        this.login = login;
        this.birthday = birthday;
    }

    public User (int id, String email, String name, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.login = login;
        this.birthday = birthday;
    }

    public void addFriends(User user) {
        friends.add(user);
    }

    public void removeFriend(User user) {
        friends.remove(user);
    }
}
