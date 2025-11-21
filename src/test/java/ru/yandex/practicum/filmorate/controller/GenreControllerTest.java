package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
public class GenreControllerTest extends ControllerTest {

    @MockBean
    GenreService genreService;

    @Test
    public void shouldGetGenre() throws Exception {
        when(genreService.getGenre(1L))
                .thenReturn(new Genre(1L, "name"));

        mockMvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
        verify(genreService).getGenre(1L);
    }

    @Test
    public void shouldReturn404WhenUserNotFoundAndGetGenre() throws Exception {
        when(genreService.getGenre(1000L))
                .thenThrow(new NotFoundException("Not Found"));
        mockMvc.perform(get("/genres/1000")).andExpect(status().isNotFound());
        verify(genreService).getGenre(any());
    }

    @Test
    public void shouldReturn400WhenNegativeIdsAndGetGenre() throws Exception {
        mockMvc.perform(get("/genres/-1")).andExpect(status().isBadRequest());
        verify(genreService, never()).getGenre(any());
    }

    @Test
    public void shouldReturn400WhenZeroUserIdAndGetGenre() throws Exception {
        mockMvc.perform(get("/genres/0")).andExpect(status().isBadRequest());
        verify(genreService, never()).getGenre(any());
    }

    @Test
    public void shouldGetGenres() throws Exception {
        Genre mpa1 = new Genre(1L, "name1");
        Genre mpa2 = new Genre(2L, "name2");

        List<Genre> genres = List.of(mpa1, mpa2);

        // Настраиваем мок сервиса
        when(genreService.getAll()).thenReturn(genres);

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("name2"));

        verify(genreService, times(1)).getAll();
    }
}
