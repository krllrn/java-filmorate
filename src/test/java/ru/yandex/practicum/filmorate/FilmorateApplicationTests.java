package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

// ----------------- USERS --------------------------

	@Test
	void badRequestWhenEmptyUserEmail() throws Exception {
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content("{\"login\": \"dolore\", \"name\": \"Nick Name\", \"email\": \"\", \"birthday\": \"1946-08-20\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void badRequestWhenNoCommercialAtInUserEmail() throws Exception {
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content("{\"login\": \"dolore\", \"name\": \"Nick Name\", \"email\": \"test_email.ru\", \"birthday\": \"1946-08-20\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void badRequestWhenUserLoginIsEmpty() throws Exception {
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content("{\"login\": \"\", \"name\": \"Nick Name\", \"email\": \"test_email@ya.ru\", \"birthday\": \"1946-08-20\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void badRequestWhenUserLoginContainBlank() throws Exception {
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content("{\"login\": \"dolore gump\", \"name\": \"Nick Name\", \"email\": \"test_email@ya.ru\", \"birthday\": \"1946-08-20\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void isOkWhenUserNameIsEmpty() throws Exception {
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content("{\"login\": \"dolore\", \"name\": \"\", \"email\": \"test_email@ya.ru\", \"birthday\": \"1946-08-20\"}"))
				.andExpect(status().isOk());
	}

	@Test
	void badRequestIfUserBirthdateInFuture() throws Exception {
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content("{\"login\": \"dolore\", \"name\": \"Nick Name\", \"email\": \"test_email@ya.ru\", \"birthday\": \"2050-08-20\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void badRequestIfUserRequestIsEmpty() throws Exception {
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

// ------------------- FILMS ----------------------------

	@Test
	void badRequestIfFilmNameIsEmpty() throws Exception {
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content("{\"name\": \"\", \"description\": \"adipisicing\", \"releaseDate\": \"1967-03-25\", \"duration\": \"100\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void badRequestIfFilmDescriptionIsMoreThan200() throws Exception {
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content("{\"name\": \"test film\", \"description\": \"fksjdfkjskfj;lsjdfklxmcvnm,nerlkwjelkfjlk" +
								"asdklfkjlsdklhlsjakhfdjklsakljdhfjkshadjkfakjsjdjdjdjdjdjaslkjhdfl;jahsdl;jkfhl;jsdhfj" +
								"ashjdfhjkasfjshfhfjsdhfjsakhfkjsahdfjksdjfhfasjkfhsdjkhfjkadfhjashfsadjkqw\", " +
								"\"releaseDate\": \"1967-03-25\", \"duration\": \"100\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void isOkIfFilmDescriptionIs200() throws Exception {
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content("{\"name\": \"test film\", \"description\": \"fksjdfkjskfj;lsjdfklxmcvnm,nerlkwjelkfjl" +
								"kasdklfkjlsdklhlsjakhfdjklsakljdhfjkshadjkfakjsjdjdjdjdjdjaslkjhdfl;jahsdl;jkfhl;jsdhfj" +
								"ashjdfhjkasfjshfhfjsdhfjsakhfkjsahdfjksdjfhfasjkfhsdjkhfjkadfhjashfsadjk\", " +
								"\"releaseDate\": \"1967-03-25\", \"duration\": \"100\"}"))
				.andExpect(status().isOk());
	}

	@Test
	void badRequestIfFilmReleaseDateWrong() throws Exception {
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content("{\"name\": \"test film\", \"description\": \"adipisicing\", \"releaseDate\": \"1895-12-27\", \"duration\": \"100\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void badRequestIfFilmDurationIsNegative() throws Exception {
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content("{\"name\": \"test film\", \"description\": \"adipisicing\", \"releaseDate\": \"1967-03-25\", \"duration\": \"-1\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void badRequestIfFilmRequestIsEmpty() throws Exception {
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
}