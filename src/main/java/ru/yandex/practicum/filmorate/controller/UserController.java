package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping()
    public List<User> returnAll() {
        return new ArrayList<>(users.values());
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
