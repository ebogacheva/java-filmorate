DROP TABLE IF EXISTS likes;
CREATE TABLE IF NOT EXISTS likes (
film_id int REFERENCES film (id) ON DELETE CASCADE,
user_id int REFERENCES users_filmorate (id) ON DELETE CASCADE,
CONSTRAINT pk_likes PRIMARY KEY (film_id, user_id)
);
DELETE FROM film;
INSERT INTO mpa (name) VALUES ('G');
INSERT INTO mpa (name) VALUES ('PG');
INSERT INTO mpa (name) VALUES ('PG-13');
INSERT INTO mpa (name) VALUES ('R');
INSERT INTO mpa (name) VALUES ('NC-17');

INSERT INTO genre (name) VALUES ('Комедия');
INSERT INTO genre (name) VALUES ('Драма');
INSERT INTO genre (name) VALUES ('Мультфильм');
INSERT INTO genre (name) VALUES('Триллер');
INSERT INTO genre (name) VALUES ('Документальный');
INSERT INTO genre (name) VALUES ('Боевик');