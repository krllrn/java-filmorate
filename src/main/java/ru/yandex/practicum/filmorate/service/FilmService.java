package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmMapRow;
import ru.yandex.practicum.filmorate.storage.InDbFilmStorage;
import ru.yandex.practicum.filmorate.storage.InDbUserStorage;

import java.util.List;

@Service
public class FilmService {
    InDbFilmStorage inDbFilmStorage;
    InDbUserStorage inDbUserStorage;
    JdbcTemplate jdbcTemplate;


    @Autowired
    public FilmService (InDbFilmStorage inDbFilmStorage, InDbUserStorage inDbUserStorage, JdbcTemplate jdbcTemplate) {
        this.inDbFilmStorage = inDbFilmStorage;
        this.inDbUserStorage = inDbUserStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    // добавление лайка
    public void addLike(int filmId, int userId) {
        if (inDbFilmStorage.getFilmById(filmId) == null || inDbUserStorage.getUserById(userId) == null) {
            throw new NotFoundException("Объект не может быть найден: " + filmId + " или " + userId);
        }
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    // удаление лайка
    public void deleteLike(int filmId, int userId) {
        if (inDbFilmStorage.getFilmById(filmId) == null || inDbUserStorage.getUserById(userId) == null) {
            throw new NotFoundException("Объект не может быть найден: " + filmId + " или " + userId);
        }
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    // 10 фильмов с бОльшим кол-вом лайков
    public List<Film> popularFilms(int count) {
        if (count > 0) {
            StringBuilder sb = new StringBuilder();
            String sqlQuery = "SELECT films.*, COUNT(likes.user_id) AS likes_count " +
                    "FROM films " +
                    "LEFT JOIN likes ON films.id = likes.film_id " +
                    "GROUP BY films.id " +
                    "ORDER BY likes_count DESC " +
                    "LIMIT " + count;
            return jdbcTemplate.query(sqlQuery, new FilmMapRow());
        } else {
            String sqlQuery = "SELECT films.*, COUNT(likes.user_id) AS likes_count " +
                    "FROM films " +
                    "LEFT JOIN likes ON films.id = likes.film_id " +
                    "GROUP BY films.id " +
                    "ORDER BY likes_count DESC " +
                    "LIMIT 10";
            return jdbcTemplate.query(sqlQuery, new FilmMapRow());
        }
    }
}
