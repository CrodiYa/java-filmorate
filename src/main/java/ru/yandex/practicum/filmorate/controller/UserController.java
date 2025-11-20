package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceInterface;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserServiceInterface userService;

    /**
     * Constructor for dependency injection
     */
    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    /**
     * Handles GET method.
     * <p>Retrieves all users from the storage.
     *
     * @return Collection of all users.
     */
    @GetMapping
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }

    /**
     * Handles GET method.
     * <p>Retrieves user from the storage.
     *
     * @param id user`s id. Must be positive number
     * @return User.
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable @Positive Long id) {
        return userService.getUser(id);
    }

    /**
     * Handles POST method.
     * <p>Creates user in storage after validation.
     *
     * @return created users.
     */
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Handles PUT method.
     * <p>Updates user in storage after validation.
     *
     * <p>Conditions:
     * <ul>
     * <li>ID must be provided in the request body</li>
     * <li>User with this ID should exist in the storage.</li>
     * </ul>
     *
     * @return updated user.
     * @throws ValidationException if ID is null or not valid
     * @throws NotFoundException   if user is not found
     */
    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    /**
     * Handles DELETE method.
     * <p>Deletes user from the storage.
     *
     * @param id user`s id. Must be positive number
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @Positive Long id) {
        userService.deleteUser(id);
    }

    /**
     * Handles GET method.
     * <p>Return collection of user`s friends.
     *
     * @param id user`s id. Must be positive number.
     * @return collection of users.
     * @throws NotFoundException if user is not found
     */
    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable @Positive Long id) {
        return userService.getFriends(id);
    }

    /**
     * Handles GET method.
     * <p>Return collection of users that are common between two users.
     *
     * @param id      user`s id. Must be positive number.
     * @param otherId other user`s id. Must be positive number.
     * @return collection of users.
     * @throws NotFoundException if user is not found
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable @Positive Long id,
                                             @PathVariable @Positive Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    /**
     * Handles PUT method.
     * <p> Creates friendship between two users.
     * User`s consent is not required.
     *
     * @param id       user`s id. Sender. Must be positive number.
     * @param friendId friend`s id. Receiver. Must be positive number.
     * @throws NotFoundException   if user is not found
     * @throws ValidationException if id equals friendId
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive Long id,
                          @PathVariable @Positive Long friendId) {
        userService.addFriend(id, friendId);
    }

    /**
     * Handles DELETE method.
     * <p> Breaks friendship between two users.
     * User`s consent is not required.
     *
     * @param id       user`s id. Sender. Must be positive number.
     * @param friendId friend`s id. Receiver. Must be positive number.
     * @throws NotFoundException if user is not found
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable @Positive Long id,
                             @PathVariable @Positive Long friendId) {
        userService.deleteFriend(id, friendId);
    }
}
