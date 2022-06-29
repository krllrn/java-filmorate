package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService implements UserDBService {
    UserStorage userStorage;

    @Autowired
    public UserService (@Qualifier("inDbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // добавление в друзья
    public void friendAdd(int userId, int friendId) {
        if (userStorage.getUserById(friendId) == null || userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с одним из id не может быть найден.");
        }
        userStorage.addFriend(userId, friendId);
    }

    // удаление из друзей
    public void friendDelete(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    // вывод списка друзей
    public Set<User> showFriends(int userId) {
        return userStorage.showFriends(userId);
    }

    // вывод списка общих друзей
    public Set<User> showCommonFriends(int userId, int friendId) {
        return userStorage.showCommonFriends(userId, friendId);
    }
}
