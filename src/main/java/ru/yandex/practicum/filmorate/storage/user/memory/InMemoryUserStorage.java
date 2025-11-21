package ru.yandex.practicum.filmorate.storage.user.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of user users.
 * <p>
 * Provides thread-safe users for User objects using ConcurrentHashMap with atomic ID generation.
 * Suitable for development and testing environments without persistent users requirements.
 * </p>
 *
 * @see User
 * @see UserStorage
 */
@Component("MemUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;
    private final AtomicLong idGenerator;

    public InMemoryUserStorage() {
        this.users = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }

    @Override
    public User add(User user) {
        user.setId(idGenerator.getAndIncrement());
        users.put(user.getId(), user);

        return users.get(user.getId());
    }

    @Override
    public User update(User newUser) {
        users.put(newUser.getId(), newUser);

        return users.get(newUser.getId());
    }

    @Override
    public User remove(Long id) {
        return users.remove(id);
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return List.copyOf(users.values());
    }

    @Override
    public boolean contains(Long id) {
        return users.containsKey(id);
    }

    @Override
    public List<User> getAllFromCollection(Collection<Long> ids) {
        return ids.stream().map(users::get).toList();
    }
}
