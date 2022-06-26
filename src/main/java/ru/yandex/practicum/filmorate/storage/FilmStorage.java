package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Qualifier("inDbFilmStorage")
public interface FilmStorage {
    List<Film> returnAll();
    Film create(Film film);
    void delete(int id);
    Film update(Film film);
    Film getFilmById(int id);
}
