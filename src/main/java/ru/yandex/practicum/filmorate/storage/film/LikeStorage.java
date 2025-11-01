package ru.yandex.practicum.filmorate.storage.film;

public interface LikeStorage {

    void initializeLikesSet(Long id);

    void clearLikesSet(Long id);

    Long addLike(Long filmId, Long userId);

    Long deleteLike(Long filmId, Long userId);
}
