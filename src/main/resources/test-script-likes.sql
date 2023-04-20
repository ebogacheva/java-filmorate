DROP TABLE IF EXISTS likes;
CREATE TABLE IF NOT EXISTS likes (
film_id int REFERENCES film (id) ON DELETE CASCADE,
user_id int REFERENCES users_filmorate (id) ON DELETE CASCADE,
CONSTRAINT pk_likes PRIMARY KEY (film_id, user_id)
);