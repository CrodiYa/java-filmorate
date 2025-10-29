package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BasicStorage;

import java.util.Collection;

public interface UserStorage extends BasicStorage<User> {

    Collection<User> addFriend(Long senderId, Long receiverId);

    void deleteFriend(Long senderId, Long receiverId);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);
}
