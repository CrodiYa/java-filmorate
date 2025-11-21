package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setLikes(resultSet.getLong("likes"));
        film.setDuration(resultSet.getInt("duration"));

        Date date = resultSet.getDate("release_date");
        LocalDate releaseDate = date == null ? null : date.toLocalDate();

        film.setReleaseDate(releaseDate);

        mapMpa(film, resultSet);
        mapGenres(film, resultSet);

        return film;
    }

    private void mapMpa(Film film, ResultSet resultSet) throws SQLException {
        Long mpaId = resultSet.getLong("mpa_id");
        if (!resultSet.wasNull()) {
            String mpaName = resultSet.getString("mpa_name");
            film.setMpa(new Mpa(mpaId, mpaName));
        }
    }

    private void mapGenres(Film film, ResultSet resultSet) throws SQLException {
        Array sqlArrayIds = resultSet.getArray("genre_ids");
        if (resultSet.wasNull()) {
            return;
        }

        Array sqlArrayNames = resultSet.getArray("genre_names");

        Object[] idsArray = (Object[]) sqlArrayIds.getArray();
        Object[] namesArray = (Object[]) sqlArrayNames.getArray();

        Set<Genre> genres = new HashSet<>();

        for (int i = 0; i < idsArray.length; i++) {
            if (idsArray[i] != null && namesArray[i] != null) {
                Long id = ((Number) idsArray[i]).longValue();
                String name = namesArray[i].toString();
                genres.add(new Genre(id, name));
            }
        }
        film.setGenres(genres);
    }
}
