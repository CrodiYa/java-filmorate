package ru.yandex.practicum.filmorate.storage.user.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDao;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Repository("DbUserStorage")
public class DbUserStorage extends BaseDao<User> implements UserStorage {

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM users;";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";
    private static final String SELECT_ALL_FROM_COLLECTION = "SELECT * FROM users WHERE user_id IN (:ids)";
    private static final String UPDATE_QUERY = """
            UPDATE users SET email = :email, login = :login, name = :name, birthday = :birthday
            WHERE user_id = :user_id
            """;

    private final SimpleJdbcInsert simpleInsert;

    public DbUserStorage(JdbcTemplate jdbc, UserMapper mapper) {
        super(jdbc, mapper);
        this.simpleInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public User add(User user) {
        Long id = simpleInsert.executeAndReturnKey(userToMap(user)).longValue();
        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {
        Map<String, Object> map = userToMap(user);
        map.put("user_id", user.getId());
        update(UPDATE_QUERY, map);
        return user;
    }

    @Override
    public User remove(Long id) {
        User user = get(id);
        remove(DELETE_QUERY, id);
        return user;
    }

    @Override
    public User get(Long id) {
        return get(SELECT_BY_ID_QUERY, id);
    }

    @Override
    public Collection<User> getAll() {
        return getAll(SELECT_ALL_QUERY);
    }

    @Override
    public boolean contains(Long id) {
        return jdbc.queryForObject(EXISTS_QUERY, Boolean.class, id);
    }

    @Override
    public List<User> getAllFromCollection(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return namedJdbc.query(SELECT_ALL_FROM_COLLECTION, Collections.singletonMap("ids", ids), mapper);
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("login", user.getLogin());
        map.put("name", user.getName());
        map.put("birthday", user.getBirthday());

        return map;
    }
}
