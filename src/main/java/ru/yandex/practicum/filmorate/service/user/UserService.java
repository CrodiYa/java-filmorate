package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendShipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Main service for user operations and business logic.
 * <p>
 * Provides comprehensive user management functionality including CRUD operations, friendship management,
 * and social features. Delegates friendship-specific operations to FriendShipService.
 * </p>
 *
 * @see User
 * @see UserStorage
 */
@Service
@Transactional
public class UserService implements UserServiceInterface {

    private final UserStorage userStorage;
    private final FriendShipStorage friendShipStorage;

    /**
     * Constructor for dependency injection
     */
    public UserService(@Qualifier("DbUserStorage") UserStorage userStorage,
                       @Qualifier("DbFriendShipStorage") FriendShipStorage friendShipStorage) {
        this.userStorage = userStorage;
        this.friendShipStorage = friendShipStorage;
    }

    /**
     * @param id ID of the user to be deleted. Must be positive and exist in the system.
     * @return User from storage.
     * @throws NotFoundException if user is not found.
     */
    @Override
    public User getUser(Long id) {
        throwIfNotFound(id);
        return userStorage.get(id);
    }

    /**
     * @return Collection of all users in storage.
     */
    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    /**
     * Add user to the storage.
     * <p>If user`s name is null, then login is set as a name.
     * <p>Initializes set for friends.
     *
     * @param user User to create.
     * @return created User.
     */
    @Override
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
     * @throws NotFoundException   if user is not found
     * @throws ValidationException if ID is null
     */
    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        throwIfNotFound(user.getId());

        return userStorage.update(user);
    }

    /**
     * Deletes user from the storage.
     * Deletes user as from friends of all users.
     *
     * @param id ID of the user to be deleted. Must be positive and exist in the system.
     * @throws NotFoundException if user not found
     */
    @Override
    public void deleteUser(Long id) {
        throwIfNotFound(id);
        friendShipStorage.deleteUserFromAllFriends(id);
        userStorage.remove(id);
    }

    /**
     * Create friendship between users. Only one side needs to confirm friendship.
     *
     * @param senderId   ID of the user that sends friendship request. Must be positive and exist in the system.
     * @param receiverId ID of the user that gets friendship request. Must be positive and exist in the system.
     * @throws NotFoundException   if user is not found
     * @throws ValidationException if senderId equals receiverId
     */
    @Override
    public void addFriend(Long senderId, Long receiverId) {
        if (Objects.equals(receiverId, senderId)) {
            throw new ValidationException("Сам себя не добавишь - никто не добавит");
        }

        throwIfNotFound(senderId);
        throwIfNotFound(receiverId);

        friendShipStorage.addFriend(senderId, receiverId);
    }

    /**
     * Deletes friendship between users without. Only one side needs to confirm deletion of friendship.
     * FriendShip stops if any of users breaks it.
     *
     * @param senderId   ID of the user from where friend should be deleted. Must be positive and exist in the system.
     * @param receiverId ID of the user that is being deleted. Must be positive and exist in the system.
     * @throws NotFoundException if user is not found
     */
    @Override
    public void deleteFriend(Long senderId, Long receiverId) {
        throwIfNotFound(senderId);
        throwIfNotFound(receiverId);

        friendShipStorage.deleteFriend(senderId, receiverId);
    }

    /**
     * @param id ID of the user to get friends. Must be positive and exist in the system.
     * @return Collection of all user`s friends.
     * @throws NotFoundException if user is not found
     */
    @Override
    public Collection<User> getFriends(Long id) {
        throwIfNotFound(id);
        return userStorage.getAllFromCollection(friendShipStorage.getFriends(id));
    }

    /**
     * Searches for common friends between two users.
     *
     * @param id      ID of the first user. Must be positive and exist in the system.
     * @param otherId ID of the second user. Must be positive and exist in the system.
     * @return Collection of all common friends between users.
     * @throws NotFoundException if user is not found
     */
    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        throwIfNotFound(id);
        throwIfNotFound(otherId);

        Set<Long> ids = friendShipStorage.getCommonFriends(id, otherId);
        return userStorage.getAllFromCollection(ids);
    }

    @Override
    public void throwIfNotFound(Long id) {
        if (!userStorage.contains(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
