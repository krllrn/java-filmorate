package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectVarException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {
    InMemoryFilmStorage inMemoryFilmStorage;
    FilmService filmService;

    @Autowired
    public FilmController (InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> returnAll() {
        return inMemoryFilmStorage.returnAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return inMemoryFilmStorage.returnAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
    }

    @PostMapping()
    public void create(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.create(film);
    }

    @PutMapping()
    public void update(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.update(film);
    }

// ----------------- LIKES ----------------------------
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null || userId == null) {
            throw new IncorrectVarException("ID не должны быть NULL!");
        }
        if (id < 0 || userId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null || userId == null) {
            throw new IncorrectVarException("ID не должны быть NULL!");
        }
        if (id < 0 || userId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> popularFilms(@PathVariable(required = false) Integer count) {
        if (count <= 0) {
            throw new IncorrectVarException("COUNT не должен быть отрицательным или равным нулю!");
        }
        return filmService.popularFilms(count);
    }
}
