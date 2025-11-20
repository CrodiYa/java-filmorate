package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @Nested
    class UserControllerGetTest {

        @Test
        public void shouldGetUsers() throws Exception {
            User user1 = new User(1L, "email1", "login1", "name1", LocalDate.now());
            User user2 = new User(2L, "email2", "login2", "name2", LocalDate.now());

            List<User> users = List.of(user1, user2);

            // Настраиваем мок сервиса
            when(userService.getAllUsers()).thenReturn(users);

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].login").value("login1"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].login").value("login2"));

            verify(userService, times(1)).getAllUsers();
        }

        @Test
        public void shouldGetEmptyList() throws Exception {

            when(userService.getAllUsers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(userService, times(1)).getAllUsers();
        }

        @Test
        public void shouldGetFriends() throws Exception {
            User user2 = new User(2L, "email2", "login2", "name2", LocalDate.now());
            User user3 = new User(3L, "email3", "login3", "name3", LocalDate.now());

            List<User> users = List.of(user2, user3);

            // Настраиваем мок сервиса
            when(userService.getFriends(1L)).thenReturn(users);

            mockMvc.perform(get("/users/1/friends"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(2))
                    .andExpect(jsonPath("$[0].login").value("login2"))
                    .andExpect(jsonPath("$[1].id").value(3))
                    .andExpect(jsonPath("$[1].login").value("login3"));

            verify(userService, times(1)).getFriends(1L);
        }

        @Test
        public void shouldReturnNotFoundWhenUserIsNotFound() throws Exception {

            // Настраиваем мок сервиса
            when(userService.getFriends(1000L))
                    .thenThrow(new NotFoundException("Пользователь с id = 1000 не найден"));


            mockMvc.perform(get("/users/1000/friends"))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).getFriends(1000L);
        }

        @Test
        public void shouldGetCommonFriends() throws Exception {
            User user2 = new User(2L, "email2", "login2", "name2", LocalDate.now());
            User user3 = new User(3L, "email3", "login3", "name3", LocalDate.now());

            List<User> users = List.of(user2, user3);

            // Настраиваем мок сервиса
            when(userService.getCommonFriends(1L, 4L)).thenReturn(users);

            mockMvc.perform(get("/users/1/friends/common/4"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(2))
                    .andExpect(jsonPath("$[0].login").value("login2"))
                    .andExpect(jsonPath("$[1].id").value(3))
                    .andExpect(jsonPath("$[1].login").value("login3"));

            verify(userService, times(1)).getCommonFriends(1L, 4L);
        }

        @Test
        public void shouldReturnNotFoundWhenUserIsNotFoundWhenCommonFriends() throws Exception {

            // Настраиваем мок сервиса
            when(userService.getCommonFriends(1000L, 1L))
                    .thenThrow(new NotFoundException("Пользователь с id = 1000 не найден"));


            mockMvc.perform(get("/users/1000/friends/common/1"))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).getCommonFriends(1000L, 1L);
        }

        @Test
        public void shouldReturnNotFoundWhenOtherUserIsNotFoundWhenCommonFriends() throws Exception {

            // Настраиваем мок сервиса
            when(userService.getCommonFriends(1L, 1000L))
                    .thenThrow(new NotFoundException("Пользователь с id = 1000 не найден"));


            mockMvc.perform(get("/users/1/friends/common/1000"))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).getCommonFriends(1L, 1000L);
        }

        @Test
        public void shouldReturn400WhenInvalidIdsInFriends() throws Exception {
            mockMvc.perform(get("/users/invalid_id/friends/common/also_invalid"))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).getCommonFriends(any(), any());
        }

        @Test
        public void shouldReturn400WhenNegativeIds() throws Exception {
            mockMvc.perform(get("/users/-1/friends/common/-2"))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).getFriends(any());
        }

        @Test
        public void shouldReturn400WhenZeroUserId() throws Exception {
            mockMvc.perform(get("/users/0/friends"))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).getFriends(any());
        }

        @Test
        public void shouldGetUser() throws Exception {
            when(userService.getUser(1L))
                    .thenReturn(new User(1L, "email", "login", "name", LocalDate.MIN));

            mockMvc.perform(get("/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.email").value("email"));
            verify(userService).getUser(1L);
        }

        @Test
        public void shouldReturn404WhenUserNotFoundAndGetUser() throws Exception {
            when(userService.getUser(1000L))
                    .thenThrow(new NotFoundException("Not Found"));
            mockMvc.perform(get("/users/1000")).andExpect(status().isNotFound());
            verify(userService).getUser(any());
        }

        @Test
        public void shouldReturn400WhenNegativeIdsAndGetUser() throws Exception {
            mockMvc.perform(get("/users/-1")).andExpect(status().isBadRequest());
            verify(userService, never()).getUser(any());
        }

        @Test
        public void shouldReturn400WhenZeroUserIdAndGetUser() throws Exception {
            mockMvc.perform(get("/users/0")).andExpect(status().isBadRequest());
            verify(userService, never()).getUser(any());
        }

        @Test
        public void shouldDeleteUser() throws Exception {
            mockMvc.perform(delete("/users/1")).andExpect(status().isOk());
            verify(userService).deleteUser(any());
        }

        @Test
        public void shouldReturn404WhenUserNotFoundAndDeleteUser() throws Exception {
            doThrow(new NotFoundException("Not Found")).when(userService).deleteUser(1000L);
            mockMvc.perform(delete("/users/1000")).andExpect(status().isNotFound());
            verify(userService).deleteUser(any());
        }

        @Test
        public void shouldReturn400WhenNegativeIdsAndDeleteUser() throws Exception {
            mockMvc.perform(delete("/users/-1")).andExpect(status().isBadRequest());
            verify(userService, never()).deleteUser(any());
        }

        @Test
        public void shouldReturn400WhenZeroUserIdAndDeleteUser() throws Exception {
            mockMvc.perform(delete("/users/0")).andExpect(status().isBadRequest());
            verify(userService, never()).deleteUser(any());
        }
    }

    @Nested
    class UserControllerPostTest {

        @Test
        public void shouldCreateUser() throws Exception {
            User user = new User(null, "email@mail.org", "login1", "name1", LocalDate.now());
            User createdUser = new User(1L, "email@mail.org", "login1", "name1", LocalDate.now());

            when(userService.addUser(any(User.class))).thenReturn(createdUser);

            String json = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.email").value("email@mail.org"))
                    .andExpect(jsonPath("$.login").value("login1"))
                    .andExpect(jsonPath("$.name").value("name1"));

            verify(userService).addUser(any(User.class));
        }

        @Test
        public void shouldReturnBadRequestWhenEmailIsNotValid() throws Exception {
            User user = new User(null, "@email", "login1", "name1", LocalDate.now());

            String json = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addUser(any(User.class));
        }

        @Test
        public void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
            User user = new User(null, " ", "login1", "name1", LocalDate.now());

            String json = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addUser(any(User.class));
        }

        @Test
        public void shouldReturnBadRequestWhenLoginContainsWhiteSpaces() throws Exception {
            User user = new User(null, "email@mail.org", " logi n 1", "name1", LocalDate.now());

            String json = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addUser(any(User.class));
        }

        @Test
        public void shouldReturnBadRequestWhenLoginBirthDayInFuture() throws Exception {
            User user = new User(null, "email@mail.org", "login1", "name1", LocalDate.now().plusDays(1));

            String json = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addUser(any(User.class));
        }

        @Test
        public void shouldReturn400WhenInvalidJsonSyntax() throws Exception {
            String invalidJson = "{ name: user, email: test@mail.ru }";

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addUser(any());
        }

        @Test
        public void shouldReturn400WhenInvalidBirthdayFormat() throws Exception {
            String invalidJson = "{\"email\":\"test@mail.ru\",\"login\":\"login\",\"name\":\"Name\",\"birthday\":\"2000/01/01\"}";

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addUser(any());
        }

        @Test
        public void shouldReturn400WhenFutureBirthday() throws Exception {
            String invalidJson = "{\"email\":\"test@mail.ru\",\"login\":\"login\",\"name\":\"Name\",\"birthday\":\"2030-01-01\"}";

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addUser(any());
        }
    }

    @Nested
    class UserControllerPutTest {

        @Test
        public void shouldUpdateUser() throws Exception {
            User userToUpdate = new User(1L, "email@mail.org", "login1", "name1", LocalDate.now());
            User updatedUser = new User(1L, "email@mail.org", "login1", "name1", LocalDate.now());

            when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

            String json = objectMapper.writeValueAsString(userToUpdate);

            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.email").value("email@mail.org"))
                    .andExpect(jsonPath("$.login").value("login1"))
                    .andExpect(jsonPath("$.name").value("name1"));

            verify(userService, times(1)).updateUser(any(User.class));
        }

        @Test
        public void shouldNotUpdateUserWhenNoIdAndReturnBadRequest() throws Exception {
            User userToUpdate = new User(null, "email@mail.org", "login1", "name1", LocalDate.now());

            String json = objectMapper.writeValueAsString(userToUpdate);

            when(userService.updateUser(userToUpdate)).thenThrow(new ValidationException(""));

            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());

            verify(userService, times(1)).updateUser(any());
        }

        @Test
        public void shouldNotUpdateUserWhenNotFound() throws Exception {
            User userToUpdate = new User(1000L, "email@mail.org", "login1", "name1", LocalDate.now());

            when(userService.updateUser(any(User.class)))
                    .thenThrow(new NotFoundException("Пользователь с id = 1000 не найден"));

            String json = objectMapper.writeValueAsString(userToUpdate);

            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).updateUser(any(User.class));
        }

        @Test
        public void shouldAddFriend() throws Exception {
            mockMvc.perform(put("/users/3/friends/2"))
                    .andExpect(status().isOk());

            verify(userService, times(1)).addFriend(any(), any());
        }

        @Test
        public void shouldReturnNotFoundWhenSenderNotFound() throws Exception {

            doThrow(new NotFoundException("Пользователь с id = 1000 не найден"))
                    .when(userService).addFriend(1000L, 1L);

            mockMvc.perform(put("/users/1000/friends/1"))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).addFriend(1000L, 1L);
        }

        @Test
        public void shouldReturnNotFoundWhenReceiverNotFound() throws Exception {

            doThrow(new NotFoundException("Пользователь с id = 1000 не найден"))
                    .when(userService).addFriend(1L, 1000L);


            mockMvc.perform(put("/users/1/friends/1000"))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).addFriend(1L, 1000L);
        }

        @Test
        public void shouldReturn400WhenInvalidFriendId() throws Exception {
            mockMvc.perform(put("/users/1/friends/not_a_number"))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).addFriend(any(), any());
        }
    }

    @Nested
    class UserControllerDeleteTest {

        @Test
        public void shouldRemoveFriend() throws Exception {
            mockMvc.perform(delete("/users/1/friends/2"))
                    .andExpect(status().isNoContent());

            verify(userService, times(1)).deleteFriend(any(), any());
        }

        @Test
        public void shouldReturnNotFoundWhenUserNotFound() throws Exception {
            doThrow(new NotFoundException("Пользователь с id = 1000 не найден"))
                    .when(userService).deleteFriend(1000L, 1L);

            mockMvc.perform(delete("/users/1000/friends/1"))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).deleteFriend(any(), any());
        }

        @Test
        public void shouldReturnNotFoundWhenOtherUserNotFound() throws Exception {
            doThrow(new NotFoundException("Пользователь с id = 1000 не найден"))
                    .when(userService).deleteFriend(1L, 1000L);

            mockMvc.perform(delete("/users/1/friends/1000"))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).deleteFriend(any(), any());
        }
    }
}