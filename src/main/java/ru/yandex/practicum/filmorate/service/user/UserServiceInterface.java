package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserServiceInterface {

    User getUser(Long id);

    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    void addFriend(Long senderId, Long receiverId);

    void deleteFriend(Long senderId, Long receiverId);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);

    void throwIfNotFound(Long id);
}
