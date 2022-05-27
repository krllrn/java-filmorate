package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage;
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService (InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // добавление лайка
    public void addLike(int filmId, int userId) {
        if (inMemoryUserStorage.returnAll().get(userId) == null || inMemoryFilmStorage.returnAll().get(filmId) == null) {

        }
        User user = inMemoryUserStorage.returnAll().get(userId);
        Film film = inMemoryFilmStorage.returnAll().get(filmId);
        Set<User> likes = film.getLikes();
        likes.add(user);
        film.setLikes(likes);
    }
    // удаление лайка
    public void deleteLike(int filmId, int userId) {
        User user = inMemoryUserStorage.returnAll().get(userId);
        Film film = inMemoryFilmStorage.returnAll().get(filmId);
        Set<User> likes = film.getLikes();
        likes.remove(user);
        film.setLikes(likes);
    }
    // 10 фильмов с бОльшим кол-вом лайков
    public List<Film> popularFilms(int count) {
        List<Film> films = inMemoryFilmStorage.returnAll();
        if (count > 0) {
            return films.stream()
                    .limit(count)
                    .sorted((f1, f2)->f1.getLikes().size() - f2.getLikes().size())
                    .collect(Collectors.toList());
        } else {
            return films.stream()
                    .limit(10)
                    .sorted((f1, f2) -> f1.getLikes().size() - f2.getLikes().size())
                    .collect(Collectors.toList());
        }
    }
}
