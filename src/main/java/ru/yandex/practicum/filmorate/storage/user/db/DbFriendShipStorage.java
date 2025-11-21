package ru.yandex.practicum.filmorate.storage.user.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.user.FriendShipStorage;

import java.util.Set;

@Repository("DbFriendShipStorage")
public class DbFriendShipStorage implements FriendShipStorage {

    private static final String SELECT_BY_ID_QUERY = "SELECT friend_id FROM friendships WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO friendships(user_id, friend_id) VALUES(?,?)";
    private static final String DELETE_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String SELECT_COMMON_FRIENDS = """
            SELECT f1.friend_id
            FROM friendships f1
            JOIN friendships f2 ON f1.friend_id = f2.friend_id
            WHERE f1.user_id = ? AND f2.user_id = ?""";


    private final JdbcTemplate jdbc;

    public DbFriendShipStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addFriend(Long senderId, Long receiverId) {
        jdbc.update(INSERT_QUERY, senderId, receiverId);
    }

    @Override
    public void deleteFriend(Long senderId, Long receiverId) {
        jdbc.update(DELETE_QUERY, senderId, receiverId);
    }

    @Override
    public Set<Long> getFriends(Long id) {
        return Set.copyOf(jdbc.queryForList(SELECT_BY_ID_QUERY, Long.class, id));
    }

    @Override
    public Set<Long> getCommonFriends(Long id, Long otherId) {
        return Set.copyOf(jdbc.queryForList(SELECT_COMMON_FRIENDS, Long.class, id, otherId));
    }
}
