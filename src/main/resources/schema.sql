DROP TABLE IF EXISTS likes, film_rating, genre, film_genre, friendship_status, friends, users, films;

CREATE TABLE IF NOT EXISTS film_rating (
	id INTEGER PRIMARY KEY,
	name VARCHAR
);

CREATE TABLE IF NOT EXISTS films (
	id SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL,
	description VARCHAR(200),
	releaseDate DATE NOT NULL,
	duration INTEGER NOT NULL,
	rating_id INTEGER REFERENCES film_rating (id),
	CONSTRAINT releaseDate_check CHECK (releaseDate > '1895-12-28'),
	CONSTRAINT duration_positive CHECK (duration > 0)
);


CREATE TABLE IF NOT EXISTS genre (
	id INTEGER PRIMARY KEY,
	name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id INTEGER REFERENCES films (id),
	genre_id INTEGER REFERENCES genre (id)
);

CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	email VARCHAR NOT NULL,
	name VARCHAR,
	login VARCHAR NOT NULL,
	birthday DATE,
	CONSTRAINT email_check CHECK (email like '%_@__%.__%'),
	CONSTRAINT login_check CHECK (login NOT like '% %'),
	CONSTRAINT birthday_check CHECK (birthday <= SYSDATE)
);


CREATE TABLE IF NOT EXISTS likes (
	film_id INTEGER REFERENCES films (id),
	user_id INTEGER REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS friendship_status (
	id INTEGER PRIMARY KEY,
	name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
	user_id INTEGER REFERENCES users (id),
	friend_id INTEGER REFERENCES users (id),
	status_id INTEGER REFERENCES friendship_status (id)
);



