package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectVarException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InDbUserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    InDbUserStorage inDbUserStorage;
    UserService userService;

    @Autowired
    public UserController (InDbUserStorage inDbUserStorage, UserService userService) {
        this.inDbUserStorage = inDbUserStorage;
        this.userService = userService;
    }

    @GetMapping()
    public List<User> returnAll() {
        return inDbUserStorage.returnAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return inDbUserStorage.getUserById(id);
    }

    @PostMapping()
    public @ResponseBody User create(@Valid @RequestBody User user) {
        return inDbUserStorage.create(user);
    }

    @PutMapping()
    public @ResponseBody User update(@Valid @RequestBody User user) {
        return inDbUserStorage.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        inDbUserStorage.delete(id);
    }

// -------------- FRIENDS ---------------------------------------
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable(required = false) int friendId) {
        if (id == friendId) {
            throw new IncorrectVarException("ID's не должны быть равны");
        }
        userService.friendAdd(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        if (id == friendId) {
            throw new IncorrectVarException("ID's не должны быть равны");
        }
        userService.friendDelete(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public @ResponseBody Set<User> friends(@PathVariable int id) {
        if (id < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        return userService.showFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public @ResponseBody Set<User> commonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (id < 0 || otherId < 0) {
            throw new IncorrectVarException("ID не должны быть отрицательными!");
        }
        if (id == otherId) {
            throw new IncorrectVarException("ID's не должны быть равны");
        }
        return userService.showCommonFriends(id, otherId);
    }
}
