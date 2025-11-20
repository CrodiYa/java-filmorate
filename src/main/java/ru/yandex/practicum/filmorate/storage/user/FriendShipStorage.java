package ru.yandex.practicum.filmorate.storage.user;

import java.util.Set;

public interface FriendShipStorage {

    default void deleteUserFromAllFriends(Long id) {
    }

    void addFriend(Long senderId, Long receiverId);

    void deleteFriend(Long senderId, Long receiverId);

    Set<Long> getFriends(Long id);

    Set<Long> getCommonFriends(Long id, Long otherId);
}
