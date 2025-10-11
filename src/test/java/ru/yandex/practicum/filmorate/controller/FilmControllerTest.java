package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest extends ControllerTest {


    private void addTestFilm() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    @BeforeEach
    public void getJsonString() throws JsonProcessingException {

        Film film = new Film(2L,
                "TestFilm",
                "TestDescription",
                LocalDate.of(2000, 1, 1),
                120);

        json = objectMapper.writeValueAsString(film);
    }

    @Nested
    class FilmControllerGetTest {

        @Test
        public void shouldGetFilms() throws Exception {
            addTestFilm();
            addTestFilm();

            mockMvc.perform(get("/films"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].name").value("TestFilm"))
                    .andExpect(jsonPath("$[1].id").value("2"))
                    .andExpect(jsonPath("$[1].name").value("TestFilm"));
        }
    }

    @Nested
    class FilmControllerPostTest {

        @Test
        public void shouldCreateFilm() throws Exception {

            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("TestFilm"))
                    .andExpect(jsonPath("$.description").value("TestDescription"))
                    .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                    .andExpect(jsonPath("$.duration").value("120"));
        }

        @Test
        public void shouldNotCreateFilmWhenInvalidJson() throws Exception {
            String json = "{}";

            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FilmControllerPutTest {

        @BeforeEach
        public void getNewText() throws JsonProcessingException {
            Film film = new Film(1L,
                    "ONLY TODAY",
                    "NEW TEXT",
                    LocalDate.of(2000, 1, 1),
                    120);

            json = objectMapper.writeValueAsString(film);
        }

        @Test
        public void shouldUpdateFilm() throws Exception {
            addTestFilm();

            mockMvc.perform(put("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("ONLY TODAY"))
                    .andExpect(jsonPath("$.description").value("NEW TEXT"))
                    .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                    .andExpect(jsonPath("$.duration").value("120"));
        }

        @Test
        public void shouldNotUpdateFilmWhenNoId() throws Exception {
            String json =
                    "{\"name\":\"test\", \"description\":\"test\", \"releaseDate\":\"2000-01-01\", \"name\":120}";
            addTestFilm();


            mockMvc.perform(put("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void shouldNotUpdateFilmWhenNotFound() throws Exception {
            mockMvc.perform(put("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());
        }

        @Test
        public void shouldNotUpdateFilmWhenInvalidJson() throws Exception {
            String json = "{}";

            mockMvc.perform(put("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }
}