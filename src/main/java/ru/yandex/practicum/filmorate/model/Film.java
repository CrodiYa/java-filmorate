package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;

import java.time.LocalDate;


/**
 * Film.
 * <p>DTO to represent film
 *
 * <p><b>Properties:
 * <ul>
 *   <li><b>id</b> - Unique identifier, automatically set by the service layer</li>
 *   <li><b>name</b> - Film's name address, must not be empty</li>
 *   <li><b>description</b> - Film's description, must not be over 200 characters</li>
 *   <li><b>releaseDate</b> - Film`s release date, must be after 1985-01-28</li>
 *   <li><b>duration</b> - Film's duration, must be positive</li>
 *   <li><b>likes</b> - Film's likes, initial value is 0, read only</li>
 * </ul>
 */

@Data
@NoArgsConstructor
public class Film {

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Название фильма не может превышать 200 символов")
    private String description;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long likes = 0L;
}
