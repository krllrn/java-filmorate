package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Qualifier("inDbUserStorage")
public interface UserStorage {
    List<User> returnAll();
    void create(User user);
    void delete(int id);
    User update(User user);
    User getUserById(int id);
}
