package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InDbFilmStorage;
import ru.yandex.practicum.filmorate.storage.InDbUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    InDbFilmStorage inDbFilmStorage;
    InDbUserStorage inDbUserStorage;


    @Autowired
    public FilmService (InDbFilmStorage inDbFilmStorage, InDbUserStorage inDbUserStorage) {
        this.inDbFilmStorage = inDbFilmStorage;
        this.inDbUserStorage = inDbUserStorage;
    }

    // добавление лайка
    public void addLike(int filmId, int userId) {
        User user = inDbUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        Film film = inDbFilmStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        film.addLikes(user);
    }

    // удаление лайка
    public void deleteLike(int filmId, int userId) {
        User user = inDbUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        Film film = inDbFilmStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        film.removeLikes(user);
    }

    // 10 фильмов с бОльшим кол-вом лайков
    public List<Film> popularFilms(int count) {
        if (count > 0) {
            return inDbFilmStorage.returnAll().stream()
                    .sorted((f1, f2)->f2.getLikes().size() - f1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return inDbFilmStorage.returnAll().stream()
                    .sorted((f1, f2)->f2.getLikes().size() - f1.getLikes().size())
                    .limit(10)
                    .collect(Collectors.toList());
        }
    }
}
