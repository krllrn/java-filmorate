package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping()
    public HashMap<Integer, User> returnAllUsers() {
        log.debug("Общее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping()
    public @ResponseBody User create(@Valid @RequestBody User user) {
        try {
            if (users.containsKey(user.getId())) {
                throw new ValidationException("User already exist!");
            }
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            log.debug("Объект для сохранения: {}", user);
            users.put(user.getId(), user);
        } catch (ValidationException e) {
            log.error("ОШИБКА: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    @PutMapping()
    public void update(@Valid @RequestBody User user) {
        try {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            log.debug("Объект для обновления: {}", user);
            users.put(user.getId(), user);
        } catch (ValidationException e) {
            log.error("ОШИБКА: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
