package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryFriendShipStorage implements FriendShipStorage {

    private final Map<Long, Set<Long>> friendships;

    public InMemoryFriendShipStorage() {
        this.friendships = new ConcurrentHashMap<>();
    }

    @Override
    public void initializeFriendsSet(Long id) {
        friendships.put(id, new HashSet<>());
    }

    @Override
    public void clearFriendsSet(Long id) {
        friendships.remove(id);
    }

    @Override
    public void addFriend(Long senderId, Long receiverId) {
        friendships.get(senderId).add(receiverId);
    }

    @Override
    public void deleteFriend(Long senderId, Long receiverId) {
        friendships.get(senderId).remove(receiverId);
    }

    @Override
    public Set<Long> getFriends(Long id) {
        return friendships.get(id);
    }
}
