package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryLikeStorage implements LikeStorage {

    private final Map<Long, Set<Long>> likes;

    public InMemoryLikeStorage() {
        this.likes = new ConcurrentHashMap<>();
    }

    @Override
    public void initializeLikesSet(Long id) {
        likes.put(id, new HashSet<>());
    }

    @Override
    public void clearLikesSet(Long id) {
        likes.remove(id);
    }

    @Override
    public Long addLike(Long filmId, Long userId) {
        likes.get(filmId).add(userId);

        return (long) likes.get(filmId).size();
    }

    @Override
    public Long deleteLike(Long filmId, Long userId) {
        likes.get(filmId).remove(userId);

        return (long) likes.get(filmId).size();
    }
}
