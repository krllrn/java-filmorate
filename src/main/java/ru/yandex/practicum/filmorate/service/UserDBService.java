package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserDBService {
    void friendAdd(int userId, int friendId);
    void friendDelete(int userId, int friendId);
    Set<User> showFriends(int userId);
    Set<User> showCommonFriends(int userId, int friendId);
}
