package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final HashMap<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    /**
     *  Private constructor to initialize ID generator with starting ID = 1.
     */
    private UserController() {
        idGenerator.set(1);
    }

    /**
     * Handles GET method.
     * <p>Retrieves all users from the storage.
     *
     * @return Collection of all users.
     */
    @GetMapping
    public Collection<User> getUsers() {
        log.info("GET /users - returning {} users", users.size());
        return users.values();
    }

    /**
     * Handles POST method.
     * <p>Creates user in storage after validation.
     *
     * @return created users.
     */
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        user.setId(idGenerator.getAndIncrement());

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        log.info("POST /users - User created: {}", user);
        users.put(user.getId(), user);

        return user;
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
     * @throws NotFoundException if user is not found
     */
    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            users.put(newUser.getId(), newUser);

            log.info("PUT /users - User updated: {}", oldUser);

            return newUser;
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}
