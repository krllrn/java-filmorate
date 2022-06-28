package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InDbFilmStorage;
import ru.yandex.practicum.filmorate.storage.InDbUserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private final InDbUserStorage inDbUserStorage;
	private final InDbFilmStorage inDbFilmStorage;

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
		inDbUserStorage.deleteAll();
		inDbFilmStorage.deleteAll();
	}

	@Test
	public void testCreateUser() {
		Optional<User> userOptional = Optional.ofNullable(inDbUserStorage.create(user));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "UserLogin")
				);
	}


	@Test
	public void testFindUserById() {
		inDbUserStorage.create(user);
		Optional<User> userOptionalT = Optional.ofNullable(inDbUserStorage.getUserById(user.getId()));

		assertThat(userOptionalT)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "UserLogin")
				);
	}

	@Test
	public void testReturnAll() {
		inDbUserStorage.create(user);
		inDbUserStorage.create(friend);

		assertEquals(2, inDbUserStorage.returnAll().size());
	}

	@Test
	public void testDeleteUserById() {
		inDbUserStorage.create(user);
		inDbUserStorage.create(friend);
		inDbUserStorage.delete(user.getId());

		assertEquals(1, inDbUserStorage.returnAll().size());
	}

	@Test
	public void testUpdateUser() {
		inDbUserStorage.create(user);
		updateUser.setId(user.getId());
		inDbUserStorage.update(updateUser);
		Optional<User> userOptional = Optional.ofNullable(inDbUserStorage.getUserById(updateUser.getId()));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "UpdateLogin")
				);
	}

	@Test
	public void testAddAndShowFriend() {
		inDbUserStorage.create(user);
		inDbUserStorage.create(friend);
		inDbUserStorage.addFriend(user.getId(), friend.getId());
		Set<User> fl = new HashSet<>(inDbUserStorage.showFriends(user.getId()));
		assertEquals(1, fl.size());
		User friendUser = new User();

		for (User u : fl) {
			friendUser = u;
		}

		assertEquals("FriendLogin", friendUser.getLogin());
	}

	@Test
	public void testDeleteFriend() {
		inDbUserStorage.create(user);
		inDbUserStorage.create(friend);
		inDbUserStorage.addFriend(user.getId(), friend.getId());
		inDbUserStorage.deleteFriend(user.getId(), friend.getId());

		assertEquals(0, inDbUserStorage.showFriends(user.getId()).size());
	}

	@Test
	public void testCreateFilm() {
		Optional<Film> filmOptional = Optional.ofNullable(inDbFilmStorage.create(film));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("name", "Film Name")
				);
	}


	@Test
	public void testFindFilmById() {
		inDbFilmStorage.create(film);
		Optional<Film> filmOptional = Optional.ofNullable(inDbFilmStorage.getFilmById(film.getId()));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("name", "Film Name")
				);
	}

	@Test
	public void testReturnAllFilms() {
		inDbFilmStorage.create(film);
		inDbFilmStorage.create(film2);

		assertEquals(2, inDbFilmStorage.returnAll().size());
	}

	@Test
	public void testDeleteFilmById() {
		inDbFilmStorage.create(film);
		inDbFilmStorage.create(film2);
		inDbFilmStorage.delete(film2.getId());

		assertEquals(1, inDbFilmStorage.returnAll().size());
	}

	@Test
	public void testUpdateFilm() {
		inDbFilmStorage.create(film);
		film2.setId(film.getId());
		inDbFilmStorage.update(film2);
		Optional<Film> filmOptional = Optional.ofNullable(inDbFilmStorage.getFilmById(film2.getId()));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("name", "Film Name2")
				);
	}
} 