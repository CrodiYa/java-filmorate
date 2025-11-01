package ru.yandex.practicum.filmorate.storage.user;

import java.util.Set;

public interface FriendShipStorage {

    void initializeFriendsSet(Long id);

    void clearFriendsSet(Long id);

    void addFriend(Long senderId, Long receiverId);

    void deleteFriend(Long senderId, Long receiverId);

    Set<Long> getFriends(Long id);
}
