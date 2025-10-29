package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

/**
 * Main service for user operations and business logic.
 * <p>
 * Provides comprehensive user management functionality including CRUD operations, friendship management,
 * and social features. Delegates friendship-specific operations to FriendShipService.
 * </p>
 *
 * @see User
 * @see UserStorage
 * @see FriendShipService
 */
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendShipService friendShipService;

    /**
     * Constructor for dependency injection
     */
    public UserService(UserStorage userStorage, FriendShipService friendShipService) {
        this.userStorage = userStorage;
        this.friendShipService = friendShipService;
    }

    /**
     * @param id ID of the user to be deleted. Must be positive and exist in the system.
     * @return User from storage.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found.
     */
    public User getUser(Long id) {
        return userStorage.get(id);
    }

    /**
     * @return Collection of all users in storage.
     */
    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    /**
     * Add user to the storage.
     * <p>If user`s name is null, then login is set as a name.
     *
     * @param user User to create.
     * @return created User.
     */
    public User addUser(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return userStorage.add(user);
    }

    /**
     * Updates user in the storage.
     *
     * @param user User to replace. User`s id must be in the system.
     * @return updated User.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found
     */
    public User updateUser(User user) {
        return userStorage.update(user);
    }

    /**
     * Deletes user from the storage.
     *
     * @param id ID of the user to be deleted. Must be positive and exist in the system.
     */
    public void deleteUser(Long id) {
        userStorage.remove(id);
    }

    /**
     * Create friendship between users. Only one side needs to confirm friendship.
     *
     * @param senderId   ID of the user that sends friendship request. Must be positive and exist in the system.
     * @param receiverId ID of the user that gets friendship request. Must be positive and exist in the system.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException   if user is not found
     * @throws ru.yandex.practicum.filmorate.exception.ValidationException if senderId equals receiverId
     */
    public Collection<User> addFriend(Long senderId, Long receiverId) {
        return friendShipService.addFriend(senderId, receiverId);
    }

    /**
     * Deletes friendship between users without. Only one side needs to confirm deletion of friendship.
     * FriendShip stops if any of users breaks it.
     *
     * @param senderId   ID of the user from where friend should be deleted. Must be positive and exist in the system.
     * @param receiverId ID of the user that is being deleted. Must be positive and exist in the system.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found
     */
    public void deleteFriend(Long senderId, Long receiverId) {
        friendShipService.deleteFriend(senderId, receiverId);
    }

    /**
     * @param id ID of the user to get friends. Must be positive and exist in the system.
     * @return Collection of all user`s friends.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found
     */
    public Collection<User> getFriends(Long id) {
        return friendShipService.getFriends(id);
    }

    /**
     * Searches for common friends between two users.
     *
     * @param id      ID of the first user. Must be positive and exist in the system.
     * @param otherId ID of the second user. Must be positive and exist in the system.
     * @return Collection of all common friends between users.
     * @throws ru.yandex.practicum.filmorate.exception.NotFoundException if user is not found
     */
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return friendShipService.getCommonFriends(id, otherId);
    }
}
