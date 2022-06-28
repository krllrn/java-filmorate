package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
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
        String sqlReturnAll = "SELECT * FROM films";
        return jdbcTemplate.query(sqlReturnAll, new FilmMapRow());
    }

    @Override
    public Film create(Film film) {
        String sqlQueryFilms = "INSERT INTO films(name, description, releaseDate, duration, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQueryFilms, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId((Integer) keyHolder.getKey());
        if (film.getGenres() != null  && !film.getGenres().isEmpty()) {
            linkToGenre(film);
        }
        return film;
    }

    public void linkToGenre(Film film) {
        String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    @Override
    public void delete(int id) {
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public void deleteAll() {
        String sqlQuery = "DELETE FROM films";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public Film update(Film film) {
        if (getFilmById(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден!");
        } else {
            String sqlQuery = "UPDATE films SET " +
                    "name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
                    "where id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
            if (film.getGenres() != null  && !film.getGenres().isEmpty()) {
                linkToGenre(film);
            }
            return film;
        }
    }

    @Override
    public Film getFilmById(int id) {
        TreeSet<Genre> filmGenres = new TreeSet<>();
        String sqlQuery = "SELECT * FROM films WHERE id = ?";
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
            String sqlQueryGenre = "SELECT * FROM genre WHERE id IN (SELECT genre_id FROM film_genre WHERE film_id = ?)";
            SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet(sqlQueryGenre, film.getId());
            while (genreRowSet.next()) {
                Genre genreFound = new Genre(
                        genreRowSet.getInt("id"),
                        genreRowSet.getString("name")
                        );
                filmGenres.add(genreFound);
            }
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            if (filmGenres.size() != 0) {
                film.setGenres(filmGenres);
            } else {
                film.setGenres(null);
            }
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Фильм с идентификатором: " + id + " не найден!");
        }
    }

    public List<Genre> returnAllGenres() {
        String sqlReturnAll = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlReturnAll, new BeanPropertyRowMapper<>(Genre.class));
    }

    public Genre returnGenreById(int id) {
        String sqlQuery = "SELECT * FROM genre WHERE id = ?";
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
        String sqlReturnAll = "SELECT * FROM film_rating";
        return jdbcTemplate.query(sqlReturnAll, new BeanPropertyRowMapper<>(Mpa.class));
    }

    public Mpa returnRatingById(int id) {
        String sqlQuery = "SELECT * FROM film_rating WHERE id = ?";
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