package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.InDbFilmStorage;

import java.util.List;

@RestController
public class RatingGenreController {
    InDbFilmStorage inDbFilmStorage;

    @Autowired
    public RatingGenreController (InDbFilmStorage inDbFilmStorage) {
        this.inDbFilmStorage = inDbFilmStorage;
    }

    @GetMapping("/genres")
    public @ResponseBody List<Genre> allGenres() {
        return inDbFilmStorage.returnAllGenres();
    }

    @GetMapping("/genres/{id}")
    public @ResponseBody Genre genreById(@PathVariable int id) {
        return inDbFilmStorage.returnGenreById(id);
    }

    @GetMapping("/mpa")
    public @ResponseBody List<Mpa> allRatings() {
        return inDbFilmStorage.returnAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public @ResponseBody Mpa ratingById(@PathVariable int id) {
        return inDbFilmStorage.returnRatingById(id);
    }
}
