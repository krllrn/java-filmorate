package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    final String dateStart = "1895-12-28";
    final LocalDate startFilmsDate = LocalDate.parse(dateStart);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId;

    public List<Film> returnAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("This film already exist!");
        }
        if (film.getReleaseDate().isBefore(startFilmsDate)) {
            throw new ValidationException("Wrong release date. Films starts from: " + startFilmsDate);
        }
        filmId = films.size() + 1;
        film.setId(filmId);
        log.debug("Объект для сохранения: {}", film);
        films.put(filmId, film);
        return film;
    }

    public void delete(int id) {
        if (!films.containsKey(id)) {
            throw new ValidationException("ID фильма не найден.");
        }
        films.remove(id);
    }

    public Film update(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(startFilmsDate)) {
            throw new ValidationException("Wrong release date. Films starts from: " + startFilmsDate);
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("ID фильма не найден!");
        }
        log.debug("Объект для обновления: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    public HashMap<Integer, Film> getFilms() {
        return films;
    }
}
