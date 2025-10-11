package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest {

    private void addTestUser() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    @BeforeEach
    public void getJsonString() throws JsonProcessingException {

        User user = new User(2L,
                "valid@mail.com",
                "login",
                "George",
                LocalDate.of(2000, 1, 1)
        );

        json = objectMapper.writeValueAsString(user);

    }

    @Nested
    class UserGetTest {

        @Test
        public void shouldGetUsers() throws Exception {
            addTestUser();
            addTestUser();

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].email").value("valid@mail.com"))
                    .andExpect(jsonPath("$[1].id").value("2"))
                    .andExpect(jsonPath("$[1].email").value("valid@mail.com"));
        }
    }

    @Nested
    class UserPostTest {

        @Test
        public void shouldCreateUser() throws Exception {

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.email").value("valid@mail.com"))
                    .andExpect(jsonPath("$.login").value("login"))
                    .andExpect(jsonPath("$.name").value("George"))
                    .andExpect(jsonPath("$.birthday").value("2000-01-01"));
        }

        @Test
        public void shouldCreateUserAndNameShouldBeLoginIfNameNull() throws Exception {

            User user = new User(2L,
                    "valid@mail.com",
                    "login",
                    null,
                    LocalDate.of(2000, 1, 1)
            );

            json = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.email").value("valid@mail.com"))
                    .andExpect(jsonPath("$.login").value("login"))
                    .andExpect(jsonPath("$.name").value("login"))
                    .andExpect(jsonPath("$.birthday").value("2000-01-01"));
        }

        @Test
        public void shouldNotCreateUserWhenInvalidJson() throws Exception {
            String json = "{}";

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UserPutTest {

        @BeforeEach
        public void getNewText() throws JsonProcessingException {
            User user = new User(2L,
                    "valid@mail.com",
                    "newlogin",
                    "same George",
                    LocalDate.of(2000, 1, 1)
            );

            json = objectMapper.writeValueAsString(user);
        }

        @Test
        public void shouldUpdateUser() throws Exception {
            addTestUser();
            addTestUser();

            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("valid@mail.com"))
                    .andExpect(jsonPath("$.login").value("newlogin"))
                    .andExpect(jsonPath("$.name").value("same George"))
                    .andExpect(jsonPath("$.birthday").value("2000-01-01"));
        }

        @Test
        public void shouldNotUpdateUserWhenNoId() throws Exception {
            String json =
                    "{\"email\":\"valid@mail.com\", \"login\":\"test\", \"name\":\"test\", \"birthday\":2000-01-01}";
            addTestUser();


            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void shouldNotUpdateUserWhenNotFound() throws Exception {
            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());
        }

        @Test
        public void shouldNotUpdateUserWhenInvalidJson() throws Exception {
            String json = "{}";

            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }
}