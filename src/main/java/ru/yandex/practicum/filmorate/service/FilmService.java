package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
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
        User user = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        Film film = inMemoryFilmStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        film.addLikes(user);
    }

    // удаление лайка
    public void deleteLike(int filmId, int userId) {
        User user = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        Film film = inMemoryFilmStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        film.removeLikes(user);
    }

    // 10 фильмов с бОльшим кол-вом лайков
    public List<Film> popularFilms(int count) {
        if (count > 0) {
            return inMemoryFilmStorage.returnAll().stream()
                    .sorted((f1, f2)->f2.getLikes().size() - f1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return inMemoryFilmStorage.returnAll().stream()
                    .sorted((f1, f2)->f2.getLikes().size() - f1.getLikes().size())
                    .limit(10)
                    .collect(Collectors.toList());
        }
    }
}
