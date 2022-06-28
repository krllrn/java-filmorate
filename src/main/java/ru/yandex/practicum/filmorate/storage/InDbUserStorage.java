package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
public class InDbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public InDbUserStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> returnAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User create(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "INSERT INTO users(email, name, login, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getName());
            ps.setString(3, user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId((Integer) keyHolder.getKey());
        return user;
    }

    @Override
    public void delete(int id) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public void deleteAll() {
        String sqlQuery = "DELETE FROM users";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public User update(User user) {
        if (getUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден!");
        } else {
            String sqlQuery = "UPDATE users SET email = ?, name = ?, login = ?, birthday = ? WHERE id = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getName(),
                    user.getLogin(),
                    user.getBirthday(),
                    user.getId());
            return user;
        }
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRowSet.next()) {
            User user = new User(
                    userRowSet.getInt("id"),
                    userRowSet.getString("email"),
                    userRowSet.getString("name"),
                    userRowSet.getString("login"),
                    LocalDate.parse(userRowSet.getString("birthday"))
            );

            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());

            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id, status_id) values(?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, 0);
    }

    public Set<User> showFriends(int userId) {
        Set fl = new HashSet<>();
        String sqlQuery = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (userRowSet.next()) {
            User friendFound = new User(userRowSet.getInt("id"),
                    userRowSet.getString("email"),
                    userRowSet.getString("name"),
                    userRowSet.getString("login"),
                    LocalDate.parse(userRowSet.getString("birthday"))
            );
            fl.add(friendFound);
        }
        return fl;
    }

    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public Set<User> showCommonFriends(int userId, int friendId) {
        Set<User> uf = showFriends(userId);
        Set<User> ff = showFriends(friendId);
        uf.retainAll(ff);

        return uf;
    }
}
