package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService (InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // добавление в друзья
    public void friendAdd(int userId, int friendId) {
        User user = inMemoryUserStorage.returnAll().get(userId);
        User friend = inMemoryUserStorage.returnAll().get(friendId);
        Set<User> friends = user.getFriends();
        friends.add(friend);
        user.setFriends(friends);
    }
    // удаление из друзей
    public void friendDelete(int userId, int friendId) {
        User user = inMemoryUserStorage.returnAll().get(userId);
        User friend = inMemoryUserStorage.returnAll().get(friendId);
        Set<User> friends = user.getFriends();
        friends.remove(friend);
        user.setFriends(friends);
    }

    // вывод списка друзей
    public Set<User> showFriends(int id) {
        return inMemoryUserStorage.returnAll().get(id).getFriends();
    }

    // вывод списка общих друзей
    public Set<User> showCommonFriends(int userId, int friendId) {
        User user = inMemoryUserStorage.returnAll().get(userId);
        User friend = inMemoryUserStorage.returnAll().get(friendId);
        Set<User> userFriends = new HashSet<>(user.getFriends());
        Set<User> friendFriends = new HashSet<>(friend.getFriends());
        userFriends.retainAll(friendFriends);
        return userFriends;
    }
}
