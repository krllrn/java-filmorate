package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectVarException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDBService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmStorage filmStorage;
    FilmDBService filmDBService;

    @Autowired
    public FilmController (@Qualifier("inDbFilmStorage")
                               FilmStorage filmStorage, FilmDBService filmDBService) {
        this.filmStorage = filmStorage;
        this.filmDBService = filmDBService;
    }

    @GetMapping()
    public List<Film> returnAll() {
        return filmStorage.returnAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return filmStorage.getFilmById(id);
    }

    @PostMapping()
    public @ResponseBody Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping()
    public @ResponseBody Film update(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        filmStorage.delete(id);
    }

// ----------------- LIKES ----------------------------
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        if (id < 0 || userId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        filmDBService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmDBService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public @ResponseBody List<Film> popularFilms(@RequestParam(required = false, defaultValue = "0") int count) {
        if (count < 0) {
            throw new IncorrectVarException("COUNT не должен быть отрицательным или равным нулю!");
        }
        return filmDBService.popularFilms(count);
    }
}
