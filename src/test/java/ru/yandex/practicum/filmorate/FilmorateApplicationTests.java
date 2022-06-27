package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InDbUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private final InDbUserStorage inDbUserStorage;

	private static User user = new User("user@mail.ru", "UserName", "UserLogin", LocalDate.parse("1988-07-27"));
	private static User friend = new User("friend@mail.ru", "FriendName", "FriendLogin", LocalDate.parse("1988-07-27"));
	private static User updateUser = new User("update@mail.ru", "UpdateName", "UpdateLogin", LocalDate.parse("1988-07-27"));

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
		Optional<User> userOptional = Optional.ofNullable(inDbUserStorage.create(user));
		Optional<User> userOptionalT = Optional.ofNullable(inDbUserStorage.getUserById(1));

		assertThat(userOptionalT)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testReturnAll() {
		Optional<User> userOptionalF = Optional.ofNullable(inDbUserStorage.create(friend));

		assertEquals(2, inDbUserStorage.returnAll().size());
	}

	@Test
	public void testDeleteUserById() {
		inDbUserStorage.returnAll().clear();
		Optional<User> userOptionalU = Optional.ofNullable(inDbUserStorage.create(user));
		Optional<User> userOptionalF = Optional.ofNullable(inDbUserStorage.create(friend));
		inDbUserStorage.delete(2);
		List<User> userList = inDbUserStorage.returnAll();

		assertEquals(1, userList.size());
	}

	@Test
	public void testUpdateUser() {
		Optional<User> userOptionalU = Optional.ofNullable(inDbUserStorage.create(user));
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
		Optional<User> userOptionalU = Optional.ofNullable(inDbUserStorage.create(user));
		Optional<User> userOptionalF = Optional.ofNullable(inDbUserStorage.create(friend));
		inDbUserStorage.addFriend(user.getId(), friend.getId());
		Set<User> fl = inDbUserStorage.showFriends(user.getId());
		assertEquals(1, fl.size());
		User friendUser = new User();

		for (User u : fl) {
			friendUser = u;
		}

		assertEquals("FriendLogin", friendUser.getLogin());
	}

	@Test
	public void testDeleteFriend() {
		Optional<User> userOptionalU = Optional.ofNullable(inDbUserStorage.create(user));
		Optional<User> userOptionalF = Optional.ofNullable(inDbUserStorage.create(friend));
		inDbUserStorage.addFriend(user.getId(), friend.getId());
		inDbUserStorage.deleteFriend(user.getId(), friend.getId());
		Set<User> fl = inDbUserStorage.showFriends(user.getId());
		assertEquals(0, fl.size());
	}
} 