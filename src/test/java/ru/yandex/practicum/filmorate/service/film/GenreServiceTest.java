package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public  class GenreServiceTest {

    @Mock
    private GenreStorage genreStorage;

    private GenreService mpaService;

    @BeforeEach
    public void setUp() {
        mpaService = new GenreService(genreStorage);
    }

    @Test
    public void shouldGetGenre() {
        Genre mpa = new Genre(1L, "name1");
        when(genreStorage.getGenre(1L)).thenReturn(Optional.of(mpa));

        Genre actualGenre = mpaService.getGenre(1L);

        assertNotNull(actualGenre);
        assertEquals(actualGenre, mpa);
        verify(genreStorage).getGenre(1L);
    }

    @Test
    public void shouldThrowNotFoundWhenNull() {
        when(genreStorage.getGenre(1L)).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class, () -> mpaService.getGenre(1L));

        verify(genreStorage).getGenre(1L);
    }

    @Test
    public void shouldThrowNotFound() {
        when(genreStorage.getGenre(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> mpaService.getGenre(1L));

        verify(genreStorage).getGenre(1L);
    }

    @Test
    public void shouldGetAll() {
        when(genreStorage.getAll()).thenReturn(List.of(new Genre(1L, "name1"), new Genre(2L, "name2")));

        List<Genre> list = (List<Genre>) mpaService.getAll();

        assertEquals(2, list.size());
        verify(genreStorage).getAll();
    }
}