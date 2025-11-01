package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface BasicStorage<T> {

    T add(T t);

    T update(T t);

    T remove(Long id);

    T get(Long id);

    Collection<T> getAll();

    boolean contains(Long id);

    int size();

}
