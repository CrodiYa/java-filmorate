package ru.yandex.practicum.filmorate.storage.user.memory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.user.FriendShipStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository("MemFriendShipStorage")
public class InMemoryFriendShipStorage implements FriendShipStorage {

    private final Map<Long, Set<Long>> friendships;

    public InMemoryFriendShipStorage() {
        this.friendships = new ConcurrentHashMap<>();
    }

    @Override
    public void deleteUserFromAllFriends(Long id) {
        friendships.get(id)
                .forEach(friendId -> friendships.getOrDefault(friendId, Collections.emptySet()).remove(id));
        friendships.remove(id);
    }

    @Override
    public void addFriend(Long senderId, Long receiverId) {
        friendships.computeIfAbsent(senderId, id -> new HashSet<>()).add(receiverId);
    }

    @Override
    public void deleteFriend(Long senderId, Long receiverId) {
        friendships.getOrDefault(senderId, Collections.emptySet()).remove(receiverId);
    }

    @Override
    public Set<Long> getFriends(Long id) {
        return friendships.getOrDefault(id, Collections.emptySet());
    }

    @Override
    public Set<Long> getCommonFriends(Long id, Long otherId) {
        Set<Long> friends1 = getFriends(id);
        Set<Long> friends2 = getFriends(otherId);
        friends1.retainAll(friends2);
        return friends1;
    }
}
