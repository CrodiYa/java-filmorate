package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Objects;

/**
 * Service for managing user friendships and social connections.
 * <p>
 * Handles friendship operations including adding/removing friends, retrieving friend lists,
 * and finding common friends between users. Maintains bidirectional friendship relationships.
 * </p>
 *
 * @see User
 * @see UserStorage
 */
@Service
public class FriendShipService {

    private final UserStorage userStorage;

    /**
     * Constructor for dependency injection
     */
    public FriendShipService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Makes connection(friendship) between two users.
     * <li>Sender adds Receiver to his friend map.
     * <li>Receiver adds Sender to his friend map.
     *
     * @param senderId   ID of the user that sends friendship request. Must be positive and exist in the system.
     * @param receiverId ID of the user that gets friendship request. Must be positive and exist in the system.
     * @return senderId`s collection of friends
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException   if user is not found
     * @throws ru.yandex.practicum.filmorate.exception.ValidationException if senderId equals receiverId
     */
    public Collection<User> addFriend(Long senderId, Long receiverId) {
        if (Objects.equals(receiverId, senderId)) {
            throw new ValidationException("Сам себя не добавишь - никто не добавит");
        }

        return userStorage.addFriend(senderId, receiverId);
    }

    /**
     * Breaks connection(friendship) between two users.
     * <li>Sender removes Receiver to his friend map.
     * <li>Receiver removes Sender to his friend map.
     *
     * @param senderId   ID of the user from where friend should be deleted. Must be positive and exist in the system.
     * @param receiverId ID of the user that is being deleted. Must be positive and exist in the system.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found
     */
    public void deleteFriend(Long senderId, Long receiverId) {
        userStorage.deleteFriend(senderId, receiverId);
    }

    /**
     * Returns user`s friends.
     *
     * @param id ID of the user to get friends. Must be positive and exist in the system.
     * @return Users`s collection of friends
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found
     */
    public Collection<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    /**
     * Finds intersection of friends between two users.
     * <p>Example: If user1 has friends [A, B, C] and user2 has friends [B, C, D],
     * returns [B, C].
     *
     * @param id      first user ID. Must be positive and exist in the system.
     * @param otherId second user ID. Must be positive and exist in the system.
     * @return common friends collection.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found
     */
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
