package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Service
public class FilmService implements FilmDBService {
    JdbcTemplate jdbcTemplate;
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService (@Qualifier("inDbFilmStorage") FilmStorage filmStorage,
                        @Qualifier("inDbUserStorage") UserStorage userStorage,
                        JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    // добавление лайка
    public void addLike(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Объект не может быть найден: " + filmId + " или " + userId);
        }
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    // удаление лайка
    public void deleteLike(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
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
