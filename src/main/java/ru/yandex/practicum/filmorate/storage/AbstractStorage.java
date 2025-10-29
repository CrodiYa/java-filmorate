package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.StorageData;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractStorage<T extends StorageData> implements BasicStorage<T> {

    private final Map<Long, T> storage;
    private final AtomicLong idGenerator;

    public AbstractStorage() {
        this.storage = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong(1);
    }

    @Override
    public T add(T value) {
        value.setId(idGenerator.getAndIncrement());
        storage.put(value.getId(), value);

        return storage.get(value.getId());
    }

    @Override
    public T update(T newUser) {
        throwIfNotFound(newUser.getId());

        storage.put(newUser.getId(), newUser);

        return storage.get(newUser.getId());
    }

    @Override
    public T remove(Long id) {
        throwIfNotFound(id);

        return storage.remove(id);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public T get(Long id) {
        throwIfNotFound(id);

        return storage.get(id);
    }

    @Override
    public Collection<T> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public boolean contains(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void throwIfNotFound(Long id) {
        if (!contains(id)) {
            throw new NotFoundException("Сущность с id = " + id + " не найдена");
        }
    }
}
