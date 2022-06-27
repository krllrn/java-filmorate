package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InDbUserStorage;

import java.util.*;

@Service
public class UserService {
    InDbUserStorage inDbUserStorage;

    @Autowired
    public UserService (InDbUserStorage inDbUserStorage) {
        this.inDbUserStorage = inDbUserStorage;
    }

    // добавление в друзья
    public void friendAdd(int userId, int friendId) {
        if (inDbUserStorage.getUserById(friendId) == null || inDbUserStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с одним из id не найден.");
        }
        inDbUserStorage.addFriend(userId, friendId);
    }

    // удаление из друзей
    public void friendDelete(int userId, int friendId) {
        inDbUserStorage.deleteFriend(userId, friendId);
    }

    // вывод списка друзей
    public Set<User> showFriends(int userId) {
        return inDbUserStorage.showFriends(userId);
    }

    // вывод списка общих друзей
    public Set<User> showCommonFriends(int userId, int friendId) {
        return inDbUserStorage.showCommonFriends(userId, friendId);
    }
}
