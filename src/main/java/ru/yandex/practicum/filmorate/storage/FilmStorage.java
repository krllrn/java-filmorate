package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> returnAll();
    Film create(Film film);
    void delete(int id);
    void deleteAll();
    Film update(Film film);
    Film getFilmById(int id);
}
