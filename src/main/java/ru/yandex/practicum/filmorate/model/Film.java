package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;

import java.time.LocalDate;

/**
 * User.
 * <p>DTO to represent user
 *
 * <p><b>Properties:
 * <ul>
 *   <li><b>id</b> - Unique identifier, automatically set by the service layer</li>
 *   <li><b>name</b> - Film's name address, must not be empty</li>
 *   <li><b>description</b> - Film's description, must not be over 200 characters</li>
 *   <li><b>releaseDate</b> - Film`s release date, must be after 1985-01-28</li>
 *   <li><b>duration</b> - Film's duration, must be positive</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Название фильма не может превышать 200 символов")
    private String description;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}
