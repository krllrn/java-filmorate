package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> returnAll();
    User create(User user);
    void delete(int id);
    void deleteAll();
    User update(User user);
    User getUserById(int id);
    void addFriend(int userId, int friendId);
    Set<User> showFriends(int userId);
    void deleteFriend(int userId, int friendId);
    Set<User> showCommonFriends(int userId, int friendId);
}
