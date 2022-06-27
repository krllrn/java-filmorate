package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectVarException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InDbFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    InDbFilmStorage inDbFilmStorage;
    FilmService filmService;

    @Autowired
    public FilmController (InDbFilmStorage inDbFilmStorage, FilmService filmService) {
        this.inDbFilmStorage = inDbFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> returnAll() {
        return inDbFilmStorage.returnAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return inDbFilmStorage.getFilmById(id);
    }

    @PostMapping()
    public @ResponseBody Film create(@Valid @RequestBody Film film) {
        return inDbFilmStorage.create(film);
    }

    @PutMapping()
    public @ResponseBody Film update(@Valid @RequestBody Film film) {
        return inDbFilmStorage.update(film);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        inDbFilmStorage.delete(id);
    }

// ----------------- LIKES ----------------------------
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        if (id < 0 || userId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public @ResponseBody List<Film> popularFilms(@RequestParam(required = false, defaultValue = "0") int count) {
        if (count < 0) {
            throw new IncorrectVarException("COUNT не должен быть отрицательным или равным нулю!");
        }
        return filmService.popularFilms(count);
    }
}
