package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BasicStorage;

import java.util.Collection;
import java.util.List;

public interface UserStorage extends BasicStorage<User> {
    List<User> getAllFromCollection(Collection<Long> ids);
}
