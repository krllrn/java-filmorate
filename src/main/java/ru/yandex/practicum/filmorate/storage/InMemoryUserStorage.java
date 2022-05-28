package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId;

    public List<User> returnAll() {
        return new ArrayList<>(users.values());
    }

    public @ResponseBody User create(User user) {
        if (users.containsKey(user.getId())) {
           throw new ValidationException("User already exist!");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userId = users.size() + 1;
        user.setId(userId);
        log.debug("Объект для сохранения: {}", user);
        users.put(userId, user);
        return user;
    }

    public @ResponseBody User update(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("ID пользователя не найден");
        }
        log.debug("Объект для обновления: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public User getUserById(int id) {
        return users.get(id);
    }
}
