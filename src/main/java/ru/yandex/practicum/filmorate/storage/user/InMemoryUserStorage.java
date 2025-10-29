package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of user storage.
 * <p>
 * Provides thread-safe storage for User objects using ConcurrentHashMap with atomic ID generation.
 * Suitable for development and testing environments without persistent storage requirements.
 * </p>
 *
 * @see User
 * @see UserStorage
 */
@Component
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {

    private final Map<Long, Map<Long, User>> friendships;

    public InMemoryUserStorage() {
        super();
        friendships = new ConcurrentHashMap<>();
    }

    @Override
    public User add(User user) {
        User returnUser = super.add(user);
        friendships.put(user.getId(), new ConcurrentHashMap<>());

        return returnUser;
    }

    @Override
    public User remove(Long id) {
        throwIfNotFound(id);

        Set<Long> friendIds = friendships.get(id).keySet();
        friendIds.forEach(friendId -> friendships.get(friendId).remove(id));
        friendships.remove(id);

        return super.remove(id);
    }

    @Override
    public void clear() {
        super.clear();
        friendships.clear();
    }

    @Override
    public void throwIfNotFound(Long id) {
        if (!contains(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public Collection<User> addFriend(Long senderId, Long receiverId) {
        User receiver = get(receiverId);
        User friend = get(senderId);

        friendships.get(receiverId).put(senderId, friend);
        friendships.get(senderId).put(receiverId, receiver);
        return friendships.get(senderId).values();
    }

    @Override
    public void deleteFriend(Long senderId, Long receiverId) {
        throwIfNotFound(senderId);
        throwIfNotFound(receiverId);

        friendships.get(senderId).remove(receiverId);
        friendships.get(receiverId).remove(senderId);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        throwIfNotFound(id);
        return friendships.get(id).values();
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        throwIfNotFound(id);
        throwIfNotFound(otherId);

        Set<Long> friends = friendships.get(id).keySet();
        Map<Long, User> otherFriends = friendships.get(otherId);

        return friends.stream()
                .filter(otherFriends::containsKey)
                .map(otherFriends::get)
                .toList();
    }
}