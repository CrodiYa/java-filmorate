package ru.yandex.practicum.filmorate.storage.film;

public interface LikeStorage {

    default void clearLikes(Long id) {
    }

    Long addLike(Long filmId, Long userId);

    Long deleteLike(Long filmId, Long userId);
}
