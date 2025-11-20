package ru.yandex.practicum.filmorate.storage.film.memory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository("MemLikeStorage")
public class InMemoryLikeStorage implements LikeStorage {

    private final Map<Long, Set<Long>> likes;

    public InMemoryLikeStorage() {
        this.likes = new ConcurrentHashMap<>();
    }


    @Override
    public void clearLikes(Long id) {
        likes.remove(id);
    }

    @Override
    public Long addLike(Long filmId, Long userId) {
        likes.computeIfAbsent(filmId, id -> new HashSet<>()).add(userId);

        return (long) likes.get(filmId).size();
    }

    @Override
    public Long deleteLike(Long filmId, Long userId) {
        likes.getOrDefault(filmId, Collections.emptySet()).remove(userId);

        return (long) likes.getOrDefault(filmId, Collections.emptySet()).size();
    }
}
