package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest extends ControllerTest {

    @MockBean
    private FilmService filmService;

    @Nested
    class FilmControllerGetTest {

        @Test
        public void shouldGetFilms() throws Exception {
            Film film1 = new Film(1L, "Film One", "Description One",
                    LocalDate.of(2020, 1, 1), 120);
            Film film2 = new Film(2L, "Film Two", "Description Two",
                    LocalDate.of(2021, 1, 1), 130);

            List<Film> films = List.of(film1, film2);

            // Настраиваем мок сервиса
            when(filmService.getAllFilms()).thenReturn(films);

            mockMvc.perform(get("/films"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].name").value("Film One"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].name").value("Film Two"));

            verify(filmService, times(1)).getAllFilms();
        }

        @Test
        public void shouldGetEmptyList() throws Exception {

            when(filmService.getAllFilms()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/films"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(filmService, times(1)).getAllFilms();
        }

        @Test
        public void shouldGetPopularFilms() throws Exception {
            Film film1 = new Film(1L, "Film One", "Description One",
                    LocalDate.of(2020, 1, 1), 120);
            Film film2 = new Film(2L, "Film Two", "Description Two",
                    LocalDate.of(2021, 1, 1), 130);
            film1.setLikes(2L);
            film2.setLikes(1L);
            List<Film> films = List.of(film1, film2);

            // Настраиваем мок сервиса
            when(filmService.getTopFilms(10L)).thenReturn(films);

            mockMvc.perform(get("/films/popular"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].likes").value(2))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].likes").value(1));

            verify(filmService, times(1)).getTopFilms(10L);
        }

        @Test
        public void shouldGetOnlyCountPopularFilms() throws Exception {
            Film film1 = new Film(1L, "Film One", "Description One",
                    LocalDate.of(2020, 1, 1), 120);
            Film film2 = new Film(2L, "Film Two", "Description Two",
                    LocalDate.of(2021, 1, 1), 130);
            film1.setLikes(2L);
            film2.setLikes(1L);

            List<Film> films = List.of(film1, film2);

            // Настраиваем мок сервиса
            when(filmService.getTopFilms(2L)).thenReturn(films);

            mockMvc.perform(get("/films/popular").param("count", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].likes").value(2))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].likes").value(1));

            verify(filmService, times(1)).getTopFilms(2L);
        }

        @Test
        public void shouldReturn400WhenInvalidCountParam() throws Exception {
            mockMvc.perform(get("/films/popular")
                            .param("count", "not_a_number"))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).getTopFilms(any());
        }

        @Test
        public void shouldReturn400WhenNegativeCount() throws Exception {
            mockMvc.perform(get("/films/popular")
                            .param("count", "-5"))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).getTopFilms(any());
        }

        @Test
        public void shouldReturn400WhenZeroCount() throws Exception {
            mockMvc.perform(get("/films/popular")
                            .param("count", "0"))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).getTopFilms(any());
        }
    }

    @Nested
    class FilmControllerPostTest {

        @Test
        public void shouldCreateFilm() throws Exception {
            Film filmToCreate = new Film(null, "TestFilm", "TestDescription",
                    LocalDate.of(2000, 1, 1), 120);

            Film createdFilm = new Film(1L, "TestFilm", "TestDescription",
                    LocalDate.of(2000, 1, 1), 120);

            when(filmService.addFilm(any(Film.class))).thenReturn(createdFilm);

            String filmJson = objectMapper.writeValueAsString(filmToCreate);

            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(filmJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("TestFilm"))
                    .andExpect(jsonPath("$.description").value("TestDescription"))
                    .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                    .andExpect(jsonPath("$.duration").value(120))
                    .andExpect(jsonPath("$.likes").value(0));

            verify(filmService).addFilm(any(Film.class));
        }

        @Test
        public void shouldNotCreateFilmWhenNameIsBlankAndReturnBadRequest() throws Exception {
            Film filmToCreate = new Film(null, "", "TestDescription",
                    LocalDate.of(2000, 1, 1), 120);

            String invalidJson = objectMapper.writeValueAsString(filmToCreate);


            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addFilm(any());
        }

        @Test
        public void shouldNotCreateFilmWhenDescriptionIsOverMaxSizeAndReturnBadRequest() throws Exception {
            Film filmToCreate = new Film(null, "name", "a".repeat(300),
                    LocalDate.of(2000, 1, 1), 120);

            String invalidJson = objectMapper.writeValueAsString(filmToCreate);


            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addFilm(any());
        }

        @Test
        public void shouldNotCreateFilmWhenReleaseDateIsInvalidAndReturnBadRequest() throws Exception {
            Film filmToCreate = new Film(null, "name", "description",
                    LocalDate.of(1000, 1, 1), 120);

            String invalidJson = objectMapper.writeValueAsString(filmToCreate);


            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addFilm(any());
        }

        @Test
        public void shouldNotCreateFilmWhenDurationIsInvalidAndReturnBadRequest() throws Exception {
            Film filmToCreate = new Film(null, "name", "description",
                    LocalDate.of(2000, 1, 1), -10);

            String invalidJson = objectMapper.writeValueAsString(filmToCreate);


            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addFilm(any());
        }

        @Test
        public void shouldNotCreateFilmWhenInvalidJsonAndReturnBadRequest() throws Exception {
            String json = "{}";

            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void shouldReturn400WhenInvalidJsonSyntax() throws Exception {
            String invalidJson = "{ invalid json syntax ;";

            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addFilm(any());
        }

        @Test
        public void shouldReturn400WhenInvalidDurationType() throws Exception {
            String invalidJson =
                    "{\"name\":\"FilmName\",\"description\":\"Description\",\"releaseDate\":\"2000-01-01\",\"duration\":\"not_a_number\"}";

            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addFilm(any());
        }

        @Test
        public void shouldReturn400WhenInvalidDateFormat() throws Exception {
            String invalidJson =
                    "{\"name\":\"FilmName\",\"description\":\"Description\",\"releaseDate\":\"2000/01/01\",\"duration\":120}";

            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addFilm(any());
        }
    }

    @Nested
    class FilmControllerPutTest {

        @Test
        public void shouldUpdateFilm() throws Exception {
            Film filmToUpdate = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);
            filmToUpdate.setLikes(5L);

            Film updatedFilm = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);
            updatedFilm.setLikes(5L);

            when(filmService.updateFilm(any(Film.class))).thenReturn(updatedFilm);

            String json = objectMapper.writeValueAsString(filmToUpdate);

            mockMvc.perform(put("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("ONLY TODAY"))
                    .andExpect(jsonPath("$.description").value("NEW TEXT"))
                    .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                    .andExpect(jsonPath("$.duration").value(120))
                    .andExpect(jsonPath("$.likes").value(5));

            verify(filmService, times(1)).updateFilm(any(Film.class));
        }

        @Test
        public void shouldNotUpdateFilmWhenNoIdAndReturnBadRequest() throws Exception {
            Film filmToUpdate = new Film(null, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);

            String json = objectMapper.writeValueAsString(filmToUpdate);


            mockMvc.perform(put("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).updateFilm(any());
        }

        @Test
        public void shouldNotUpdateFilmWhenNotFound() throws Exception {
            Film filmToUpdate = new Film(1000L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);

            when(filmService.updateFilm(any(Film.class)))
                    .thenThrow(new NotFoundException("Фильм с id = 1000 не найден"));

            String json = objectMapper.writeValueAsString(filmToUpdate);

            mockMvc.perform(put("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());

            verify(filmService, times(1)).updateFilm(any(Film.class));
        }

        @Test
        public void shouldAddLike() throws Exception {
            Film film = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);
            film.setLikes(5L);

            Film updatedFilm = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);
            updatedFilm.setLikes(6L);

            when(filmService.addLike(any(), any())).thenReturn(updatedFilm);

            String json = objectMapper.writeValueAsString(film);

            mockMvc.perform(put("/films/1/like/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("ONLY TODAY"))
                    .andExpect(jsonPath("$.description").value("NEW TEXT"))
                    .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                    .andExpect(jsonPath("$.duration").value(120))
                    .andExpect(jsonPath("$.likes").value(6));

            verify(filmService, times(1)).addLike(any(), any());
        }

        @Test
        public void shouldNotAddLikeWhenFilmNotFound() throws Exception {
            Film filmToUpdate = new Film(1000L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);

            when(filmService.addLike(any(), any()))
                    .thenThrow(new NotFoundException("Фильм с id = 1000 не найден"));

            String json = objectMapper.writeValueAsString(filmToUpdate);

            mockMvc.perform(put("/films/1000/like/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());

            verify(filmService, times(1)).addLike(any(), any());
        }

        @Test
        public void shouldNotAddLikeWhenUserNotFound() throws Exception {
            Film filmToUpdate = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);

            when(filmService.addLike(any(), any()))
                    .thenThrow(new NotFoundException("Пользователь с id = 1000 не найден"));

            String json = objectMapper.writeValueAsString(filmToUpdate);

            mockMvc.perform(put("/films/1/like/1000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());

            verify(filmService, times(1)).addLike(any(), any());
        }

        @Test
        public void shouldReturn400WhenInvalidIdsInLike() throws Exception {
            mockMvc.perform(put("/films/invalid_id/like/also_invalid"))
                    .andExpect(status().isBadRequest());

            verify(filmService, never()).addLike(any(), any());
        }
    }

    @Nested
    class FilmControllerDeleteTest {

        @Test
        public void shouldRemoveLike() throws Exception {
            Film film = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);
            film.setLikes(5L);

            Film updatedFilm = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);
            updatedFilm.setLikes(4L);

            when(filmService.removeLike(any(), any())).thenReturn(updatedFilm);

            String json = objectMapper.writeValueAsString(film);

            mockMvc.perform(delete("/films/1/like/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("ONLY TODAY"))
                    .andExpect(jsonPath("$.description").value("NEW TEXT"))
                    .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                    .andExpect(jsonPath("$.duration").value(120))
                    .andExpect(jsonPath("$.likes").value(4));

            verify(filmService, times(1)).removeLike(any(), any());
        }

        @Test
        public void shouldNotRemoveLikeWhenFilmNotFound() throws Exception {
            Film filmToUpdate = new Film(1000L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);

            when(filmService.removeLike(any(), any()))
                    .thenThrow(new NotFoundException("Фильм с id = 1000 не найден"));

            String json = objectMapper.writeValueAsString(filmToUpdate);

            mockMvc.perform(delete("/films/1000/like/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());

            verify(filmService, times(1)).removeLike(any(), any());
        }

        @Test
        public void shouldNotRemoveLikeWhenUserNotFound() throws Exception {
            Film filmToUpdate = new Film(1L, "ONLY TODAY", "NEW TEXT",
                    LocalDate.of(2000, 1, 1), 120);

            when(filmService.removeLike(any(), any()))
                    .thenThrow(new NotFoundException("Пользователь с id = 1000 не найден"));

            String json = objectMapper.writeValueAsString(filmToUpdate);

            mockMvc.perform(delete("/films/1/like/1000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());

            verify(filmService, times(1)).removeLike(any(), any());
        }
    }
}