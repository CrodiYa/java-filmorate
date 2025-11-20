INSERT INTO mpa (name) VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO genres (name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

INSERT INTO users (email, login, name, birthday) VALUES
('email1', 'login1', 'name1', '2000-01-01'),
('email2', 'login2', 'name2', '2000-01-01'),
('email3', 'login3', 'name3', '2000-01-01'),
('email4', 'login4', 'name4', '2000-01-01'),
('email5', 'login5', 'name5', NULL);

INSERT INTO friendships(user_id, friend_id) VALUES
(1L, 2L),
(2L, 1L),
(3L, 1L),
(3L, 4L);

INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES
('name1', 'desc1', '2000-01-01', 100, 1),
('name2', 'desc2', '2000-01-01', 100, 2),
('name3', 'desc3', '2000-01-01', 100, 3),
('name4', 'desc4', '2000-01-01', 100, 4),
('name5', 'desc5', '2000-01-01', 100, 5);

INSERT INTO films_genres (film_id, genre_id) VALUES
(4,1),
(5,1),
(5,2),
(5,3);

INSERT INTO likes (film_id, user_id) VALUES
(1,1),
(1,2),
(2,1),
(2,3),
(2,4),
(3,1),
(3,2),
(3,3),
(3,4);