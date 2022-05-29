package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService (InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // добавление в друзья
    public void friendAdd(int userId, int friendId) {
        User user = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        User friend = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == friendId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        user.addFriends(friend);
        friend.addFriends(user);
    }

    // удаление из друзей
    public void friendDelete(int userId, int friendId) {
        User user = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        User friend = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == friendId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        user.removeFriend(friend);
        friend.removeFriend(user);
    }

    // вывод списка друзей
    public Set<User> showFriends(int userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Объект не найден!");
        }
        return inMemoryUserStorage.getUserById(userId).getFriends();
    }

    // вывод списка общих друзей
    public Set<User> showCommonFriends(int userId, int friendId) {
        User user = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        User friend = inMemoryUserStorage.returnAll().parallelStream()
                .filter(x -> x.getId() == friendId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Объект не найден!"));
        Set<User> userSet = new HashSet<>(user.getFriends());
        Set<User> friendSet = new HashSet<>(friend.getFriends());
        userSet.retainAll(friendSet);
        return userSet;
    }
}
