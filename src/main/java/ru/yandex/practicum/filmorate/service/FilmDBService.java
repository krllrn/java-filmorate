package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Qualifier("FilmService")
public interface FilmDBService {
    void addLike(int filmId, int userId);
    void deleteLike(int filmId, int userId);
    List<Film> popularFilms(int count);
}
