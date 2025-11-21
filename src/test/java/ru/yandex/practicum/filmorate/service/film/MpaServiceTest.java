package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MpaServiceTest {

    @Mock
    private MpaStorage mpaStorage;

    private MpaService mpaService;

    @BeforeEach
    public void setUp() {
        mpaService = new MpaService(mpaStorage);
    }

    @Test
    public void shouldGetMpa() {
        Mpa mpa = new Mpa(1L, "name1");
        when(mpaStorage.getMpa(1L)).thenReturn(Optional.of(mpa));

        Mpa actualMpa = mpaService.getMpa(1L);

        assertNotNull(actualMpa);
        assertEquals(actualMpa, mpa);
        verify(mpaStorage).getMpa(1L);
    }

    @Test
    public void shouldThrowNotFoundWhenNull() {
        when(mpaStorage.getMpa(1L)).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class, () -> mpaService.getMpa(1L));

        verify(mpaStorage).getMpa(1L);
    }

    @Test
    public void shouldThrowNotFound() {
        when(mpaStorage.getMpa(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> mpaService.getMpa(1L));

        verify(mpaStorage).getMpa(1L);
    }

    @Test
    public void shouldGetAll() {
        when(mpaStorage.getAll()).thenReturn(List.of(new Mpa(1L, "name1"), new Mpa(2L, "name2")));

        List<Mpa> list = (List<Mpa>) mpaService.getAll();

        assertEquals(2, list.size());
        verify(mpaStorage).getAll();
    }
}
