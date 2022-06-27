package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

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
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void create(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into users(email, name, login, birthday) " + "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday());
    }

    @Override
    public void delete(int id) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User update(User user) {
        if (getUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден!");
        } else {
            String sqlQuery = "update users set " +
                    "email = ?, name = ?, login = ?, birthday = ?" +
                    "where id = ?";
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
        String sqlQuery = "select * from users where id = ?";
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

    public User getUserByLogin(User user) {
        String sqlQuery = "select * from users where login = ?";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(sqlQuery, user.getLogin());
        if (userRowSet.next()) {
            User userFound = new User(userRowSet.getInt("id"),
                    userRowSet.getString("email"),
                    userRowSet.getString("name"),
                    userRowSet.getString("login"),
                    LocalDate.parse(userRowSet.getString("birthday"))
            );

            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());

            return userFound;
        } else {
            log.info("Пользователь с login {} не найден.", user.getLogin());
            return null;
        }
    }

    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into friends (user_id, friend_id, status_id)" + "values(?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, 0);
    }

    public Set<User> showFriends(int userId) {
        Set fl = new HashSet<>();
        String sqlQuery = "select * from users where id in (select friend_id from friends where user_id = ?)";
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
        String sqlQuery = "delete from friends where user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public Set<User> showCommonFriends(int userId, int friendId) {
        Set uf = showFriends(userId);
        Set ff = showFriends(friendId);
        uf.retainAll(ff);

        return uf;
    }
}
