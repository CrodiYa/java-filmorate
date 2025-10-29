package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.ValidLogin;

import java.time.LocalDate;

/**
 * User.
 * <p>DTO to represent user
 *
 * <p><b>Properties:
 * <ul>
 *   <li><b>id</b> - Unique identifier, automatically set by the service layer</li>
 *   <li><b>email</b> - User's email address, must be valid and not empty</li>
 *   <li><b>login</b> - User's login identifier, must not be empty</li>
 *   <li><b>name</b> - Display name; if null or blank, service will set it to login value</li>
 *   <li><b>birthday</b> - User's birthdate, must be in the past or present</li>
 * </ul>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends StorageData {

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        super(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @NotBlank(message = "Почта не может быть пустой")
    @Email
    private String email;

    @ValidLogin
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
