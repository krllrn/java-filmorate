package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId;

    public List<User> returnAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
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

    public void delete(int id) {
        if (!users.containsKey(id)) {
            throw new ValidationException("ID пользователя не найден.");
        }
        users.remove(id);
    }

    @Override
    public void deleteAll() {

    }

    public User update(User user) {
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

    public User getUserById(int id) {
        return null;
    }

    @Override
    public void addFriend(int userId, int friendId) {

    }

    @Override
    public Set<User> showFriends(int userId) {
        return null;
    }

    @Override
    public void deleteFriend(int userId, int friendId) {

    }

    @Override
    public Set<User> showCommonFriends(int userId, int friendId) {
        return null;
    }
}
