package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.IncorrectVarException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    InMemoryUserStorage inMemoryUserStorage;
    UserService userService;

    @Autowired
    public UserController (InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping()
    public List<User> returnAll() {
        return inMemoryUserStorage.returnAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return inMemoryUserStorage.returnAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
    }

    @PostMapping()
    public @ResponseBody User create(@Valid @RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping()
    public @ResponseBody User update(@Valid @RequestBody User user) {
        return inMemoryUserStorage.update(user);
    }

// -------------- FRIENDS ---------------------------------------
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (id == null || friendId == null) {
            throw new IncorrectVarException("ID не должны быть NULL!");
        }
        if (id < 0 || friendId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        if (id.equals(friendId)) {
            throw new IncorrectVarException("ID's не должны быть равны");
        }
        userService.friendAdd(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (id == null || friendId == null) {
            throw new IncorrectVarException("ID не должны быть NULL!");
        }
        if (id < 0 || friendId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        if (id.equals(friendId)) {
            throw new IncorrectVarException("ID's не должны быть равны");
        }
        userService.friendDelete(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> friends(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectVarException("ID не должны быть NULL!");
        }
        if (id < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        return userService.showFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> commonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (id == null || otherId == null) {
            throw new IncorrectVarException("ID не должны быть NULL!");
        }
        if (id < 0 || otherId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        if (id.equals(otherId)) {
            throw new IncorrectVarException("ID's не должны быть равны");
        }
        return userService.showCommonFriends(id, otherId);
    }


}
