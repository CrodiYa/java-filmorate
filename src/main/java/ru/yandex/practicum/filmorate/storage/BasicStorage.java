package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.StorageData;

import java.util.Collection;

public interface BasicStorage<T extends StorageData> {

    T add(T t);

    T update(T t);

    T remove(Long id);

    void clear();

    T get(Long id);

    Collection<T> getAll();

    boolean contains(Long id);

    int size();

    void throwIfNotFound(Long id);
}
