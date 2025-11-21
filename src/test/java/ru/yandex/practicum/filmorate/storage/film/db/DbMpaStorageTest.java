package ru.yandex.practicum.filmorate.storage.film.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.MpaMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({DbMpaStorage.class, MpaMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbMpaStorageTest {

    private final DbMpaStorage storage;

    @Test
    public void shouldGetMpa() {

        Optional<Mpa> mpaOptional = storage.getMpa(1L);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    public void shouldGetEmptyMpa() {
        Optional<Mpa> mpaOptional = storage.getMpa(1000L);
        assertThat(mpaOptional).isEmpty();
    }

    @Test
    public void shouldGetAll() {
        List<Mpa> list = (List<Mpa>) storage.getAll();
        assertEquals(5, list.size());
        assertEquals("NC-17", list.getLast().getName());
    }
}
