package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
class FilmoRateApplicationTests {
	private final UserStorage userStorage;
	private final FilmStorage filmStorage;

	@Autowired
	public FilmoRateApplicationTests (@Qualifier("inDbUserStorage") UserStorage userStorage,
									  @Qualifier("inDbFilmStorage") FilmStorage filmStorage) {
		this.userStorage = userStorage;
		this.filmStorage = filmStorage;
	}

	private final User user = new User("user@mail.ru", "UserName", "UserLogin",
			LocalDate.parse("1988-07-27"));
	private final User friend = new User("friend@mail.ru", "FriendName", "FriendLogin",
			LocalDate.parse("1988-07-27"));
	private final User updateUser = new User("update@mail.ru", "UpdateName", "UpdateLogin",
			LocalDate.parse("1988-07-27"));

	private final Mpa mpa = new Mpa(1);
	private final Film film = new Film("Film Name", "Description", LocalDate.parse("1988-07-27"), 160, mpa);
	private final Film film2 = new Film("Film Name2", "Description2", LocalDate.parse("1988-07-27"), 160, mpa);

	@BeforeEach
	public void clear() {
		userStorage.deleteAll();
		filmStorage.deleteAll();
	}

	@Test
	public void testCreateUser() {
		Optional<User> userOptional = Optional.ofNullable(userStorage.create(user));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "UserLogin")
				);
	}


	@Test
	public void testFindUserById() {
		userStorage.create(user);
		Optional<User> userOptionalT = Optional.ofNullable(userStorage.getUserById(user.getId()));

		assertThat(userOptionalT)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "UserLogin")
				);
	}

	@Test
	public void testReturnAll() {
		userStorage.create(user);
		userStorage.create(friend);

		assertEquals(2, userStorage.returnAll().size());
	}

	@Test
	public void testDeleteUserById() {
		userStorage.create(user);
		userStorage.create(friend);
		userStorage.delete(user.getId());

		assertEquals(1, userStorage.returnAll().size());
	}

	@Test
	public void testUpdateUser() {
		userStorage.create(user);
		updateUser.setId(user.getId());
		userStorage.update(updateUser);
		Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(updateUser.getId()));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "UpdateLogin")
				);
	}

	@Test
	public void testAddAndShowFriend() {
		userStorage.create(user);
		userStorage.create(friend);
		userStorage.addFriend(user.getId(), friend.getId());
		Set<User> fl = new HashSet<>(userStorage.showFriends(user.getId()));
		assertEquals(1, fl.size());
		User friendUser = new User();

		for (User u : fl) {
			friendUser = u;
		}

		assertEquals("FriendLogin", friendUser.getLogin());
	}

	@Test
	public void testDeleteFriend() {
		userStorage.create(user);
		userStorage.create(friend);
		userStorage.addFriend(user.getId(), friend.getId());
		userStorage.deleteFriend(user.getId(), friend.getId());

		assertEquals(0, userStorage.showFriends(user.getId()).size());
	}

	@Test
	public void testCreateFilm() {
		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.create(film));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("name", "Film Name")
				);
	}


	@Test
	public void testFindFilmById() {
		filmStorage.create(film);
		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(film.getId()));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("name", "Film Name")
				);
	}

	@Test
	public void testReturnAllFilms() {
		filmStorage.create(film);
		filmStorage.create(film2);

		assertEquals(2, filmStorage.returnAll().size());
	}

	@Test
	public void testDeleteFilmById() {
		filmStorage.create(film);
		filmStorage.create(film2);
		filmStorage.delete(film2.getId());

		assertEquals(1, filmStorage.returnAll().size());
	}

	@Test
	public void testUpdateFilm() {
		filmStorage.create(film);
		film2.setId(film.getId());
		filmStorage.update(film2);
		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(film2.getId()));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("name", "Film Name2")
				);
	}
} 