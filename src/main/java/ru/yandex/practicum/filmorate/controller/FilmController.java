package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    final String dateStart = "1895-12-28";
    final LocalDate startFilmsDate = LocalDate.parse(dateStart);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public List<Film> returnAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public void create(@Valid @RequestBody Film film) {
        try {
            if (films.containsKey(film.getId())) {
                throw new ValidationException("This film already exist!");
            }
            if (film.getReleaseDate().isBefore(startFilmsDate)) {
                throw new ValidationException("Wrong release date. Films starts from: " + startFilmsDate);
            }
            log.debug("Объект для сохранения: {}", film);
            films.put(film.getId(), film);
        } catch (ValidationException e) {
            log.error("ОШИБКА: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
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
