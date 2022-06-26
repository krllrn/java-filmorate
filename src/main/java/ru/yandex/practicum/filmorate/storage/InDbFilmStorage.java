package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
public class InDbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public InDbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> returnAll() {
        String sqlReturnAll = "select * from films";
        return jdbcTemplate.query(sqlReturnAll, new BeanPropertyRowMapper<>(Film.class));
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into films(name, description, releaseDate, duration, rating_id) " + "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        return film;
    }

    @Override
    public void delete(int id) {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film update(Film film) {
        if (getFilmById(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден!");
        } else {
            String sqlQuery = "update films set " +
                    "name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
                    "where id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            return film;
        }
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "select * from films where id = ?";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRowSet.next()) {
            Film film = new Film(
                    filmRowSet.getInt("id"),
                    filmRowSet.getString("name"),
                    filmRowSet.getString("description"),
                    LocalDate.parse(filmRowSet.getString("releaseDate")),
                    filmRowSet.getInt("duration"),
                    returnRatingById(filmRowSet.getInt("rating_id"))
            );

            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return null;
        }
    }

    public Film getFilmByName(Film film) {
        String sqlQuery = "select * from films where name = ?";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(sqlQuery, film.getName());
        if (filmRowSet.next()) {
            Film filmFound = new Film(
                    filmRowSet.getInt("id"),
                    filmRowSet.getString("name"),
                    filmRowSet.getString("description"),
                    LocalDate.parse(filmRowSet.getString("releaseDate")),
                    filmRowSet.getInt("duration"),
                    returnRatingById(filmRowSet.getInt("rating_id"))
            );
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return filmFound;
        } else {
            log.info("Фильм с названием {} не найден.", film.getName());
            return null;
        }
    }

    public List<Genre> returnAllGenres() {
        String sqlReturnAll = "select * from genre";
        return jdbcTemplate.query(sqlReturnAll, new BeanPropertyRowMapper<>(Genre.class));
    }

    public Genre returnGenreById(int id) {
        String sqlQuery = "select * from genre where id = ?";
        SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (genreRowSet.next()) {
            Genre genre = new Genre(
                    genreRowSet.getInt("id"),
                    genreRowSet.getString("name")
            );
            return genre;
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundException("Жанр не найден");
        }
    }

    public List<Mpa> returnAllRatings() {
        String sqlReturnAll = "select * from film_rating";
        return jdbcTemplate.query(sqlReturnAll, new BeanPropertyRowMapper<>(Mpa.class));
    }

    public Mpa returnRatingById(int id) {
        String sqlQuery = "select * from film_rating where id = ?";
        SqlRowSet ratingRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (ratingRowSet.next()) {
            Mpa rating = new Mpa(
                    ratingRowSet.getInt("id"),
                    ratingRowSet.getString("name")
            );
            return rating;
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", id);
            throw new NotFoundException("Рейтинг не найден");
        }
    }


}
