package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MpaController.class)
public class MpaControllerTest extends ControllerTest {

    @MockBean
    MpaService mpaService;

    @Test
    public void shouldGetMpa() throws Exception {
        when(mpaService.getMpa(1L))
                .thenReturn(new Mpa(1L, "name"));

        mockMvc.perform(get("/mpa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
        verify(mpaService).getMpa(1L);
    }

    @Test
    public void shouldReturn404WhenUserNotFoundAndGetMpa() throws Exception {
        when(mpaService.getMpa(1000L))
                .thenThrow(new NotFoundException("Not Found"));
        mockMvc.perform(get("/mpa/1000")).andExpect(status().isNotFound());
        verify(mpaService).getMpa(any());
    }

    @Test
    public void shouldReturn400WhenNegativeIdsAndGetMpa() throws Exception {
        mockMvc.perform(get("/mpa/-1")).andExpect(status().isBadRequest());
        verify(mpaService, never()).getMpa(any());
    }

    @Test
    public void shouldReturn400WhenZeroUserIdAndGetMpa() throws Exception {
        mockMvc.perform(get("/mpa/0")).andExpect(status().isBadRequest());
        verify(mpaService, never()).getMpa(any());
    }

    @Test
    public void shouldGetMpas() throws Exception {
        Mpa mpa1 = new Mpa(1L, "name1");
        Mpa mpa2 = new Mpa(2L, "name2");

        List<Mpa> mpas = List.of(mpa1, mpa2);

        // Настраиваем мок сервиса
        when(mpaService.getAll()).thenReturn(mpas);

        mockMvc.perform(get("/mpa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("name2"));

        verify(mpaService, times(1)).getAll();
    }
}
