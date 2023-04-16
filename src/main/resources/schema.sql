DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS film;
DROP TABLE IF EXISTS users_filmorate;
DROP TABLE IF EXISTS rating;

CREATE TABLE users_filmorate (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email varchar(255) NOT NULL,
  login varchar(255) NOT NULL,
  name varchar(255),
  birthday date
);

CREATE TABLE friendship (
  user1_id int,
  user2_id int,
  CONSTRAINT pk_friendship PRIMARY KEY (user1_id, user2_id)
);

CREATE TABLE film (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(255) NOT NULL,
  description varchar(200),
  release_date date, --COMMENT 'later than 28/12/1895'
  duration int, --int COMMENT 'more than 0'
  rating_id int
);

CREATE TABLE rating (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(255)
);

CREATE TABLE genre (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(255)
);

CREATE TABLE film_genre (
  film_id int,
  genre_id int,
  CONSTRAINT pk_film_genre PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE likes (
  film_id int,
  user_id int,
  CONSTRAINT pk_likes PRIMARY KEY (film_id, user_id)
);

ALTER TABLE friendship ADD FOREIGN KEY (user1_id) REFERENCES users_filmorate (id);

ALTER TABLE friendship ADD FOREIGN KEY (user2_id) REFERENCES users_filmorate (id);

ALTER TABLE film ADD FOREIGN KEY (rating_id) REFERENCES rating (id);

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES film (id);

ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genre (id);

ALTER TABLE likes ADD FOREIGN KEY (film_id) REFERENCES film (id);

ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users_filmorate (id);