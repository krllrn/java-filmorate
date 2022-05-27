package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
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

    public void create(@Valid @RequestBody Film film) {
        try {
            if (films.containsKey(film.getId())) {
                throw new ValidationException("This film already exist!");
            }
            if (film.getReleaseDate().isBefore(startFilmsDate)) {
                throw new ValidationException("Wrong release date. Films starts from: " + startFilmsDate);
            }
            filmId = films.size() + 1;
            film.setId(filmId);
            log.debug("Объект для сохранения: {}", film);
            films.put(film.getId(), film);
        } catch (ValidationException e) {
            log.error("ОШИБКА: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void update(@Valid @RequestBody Film film) {
        try {
            if (film.getReleaseDate().isBefore(startFilmsDate)) {
                throw new ValidationException("Wrong release date. Films starts from: " + startFilmsDate);
            }
            log.debug("Объект для обновления: {}", film);
            films.put(film.getId(), film);
        } catch (ValidationException e) {
            log.error("ОШИБКА: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
