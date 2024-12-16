package superapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import superapp.data.UserId;
import superapp.restApi.boundaries.NewUserBoundary;
import superapp.restApi.boundaries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersTests {

	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.baseUrl = "http://localhost:" + this.port;
		this.restTemplate = new RestTemplate();
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete(this.baseUrl + "/superapp/admin/users");
	}

	// test post using get operation
	@Test
	public void testSuccessfulCreateUser() throws Exception {
		// GIVEN the server is up
		// AND the database is empty
		// WHEN I POST /superapp/users using {
		// "email" : "daniel@mail.com"
		// "role" : "ADMIN",
		// "username" : "Daniel",
		// "avatar" : "db"
		// }
		NewUserBoundary newUserBoundary = new NewUserBoundary();
		newUserBoundary.setEmail("daniel@mail.com");
		newUserBoundary.setRole("ADMIN");
		newUserBoundary.setUsername("Daniel");
		newUserBoundary.setAvatar("db");
		this.restTemplate.postForObject(this.baseUrl + "/superapp/users", newUserBoundary, NewUserBoundary.class);

		// THEN the user is stored to the database
		UserBoundary userEntityFromDb = this.restTemplate.getForObject(
				this.baseUrl + "/superapp/users/login/2023b.shir.zur/daniel@mail.com", UserBoundary.class);
		assertThat(userEntityFromDb).isNotNull().extracting("userId.email", "role", "username", "avatar")
//			.isEqualTo(newMessage.getMessage());
				.containsExactly(newUserBoundary.getEmail(), newUserBoundary.getRole(), newUserBoundary.getUsername(),
						newUserBoundary.getAvatar());
	}

	@Test
	public void testGettingUser() throws Exception {
		// GIVEN the server is up
		// AND a user with an email "daniel@mail.com" is exists in the database
		NewUserBoundary newUserBoundary = new NewUserBoundary();
		newUserBoundary.setEmail("daniel@mail.com");
		newUserBoundary.setRole("ADMIN");
		newUserBoundary.setUsername("Daniel");
		newUserBoundary.setAvatar("db");
		this.restTemplate.postForObject(this.baseUrl + "/superapp/users", newUserBoundary, NewUserBoundary.class);
		// WHEN I GET /superapp/users/login/2023b.shir.zur/daniel@mail.com
		UserBoundary userBoundary = this.restTemplate.getForObject(
				this.baseUrl + "/superapp/users/login/2023b.shir.zur/daniel@mail.com", UserBoundary.class);

		// THEN the server responds with status 2xx
		// AND the returned value is userBoundary
		if (userBoundary == null) {
			throw new Exception("User does not exist in the data base");
		}
	}

	@Test
	public void testUpdateUser() throws Exception {
		// GIVEN the server is up
		// AND a user with an email "daniel@mail.com" is exists in the database
		NewUserBoundary newUserBoundary = new NewUserBoundary("Daniel", "db", "daniel@mail.com", "ADMIN");
		this.restTemplate.postForObject(this.baseUrl + "/superapp/users", newUserBoundary, NewUserBoundary.class);
		// WHEN I PUT /superapp/users/2023b.shir.zur/daniel@mail.com with different
		// username
		UserBoundary userBoundary = new UserBoundary("ADMIN", "Tal", "db", new UserId("daniel@mail.com"));
		this.restTemplate.put(this.baseUrl + "/superapp/users/2023b.shir.zur/daniel@mail.com", userBoundary);
		// THEN the user's username will be modified in the database
		UserBoundary userFromDB = this.restTemplate.getForObject(
				this.baseUrl + "/superapp/users/login/2023b.shir.zur/daniel@mail.com", UserBoundary.class);
		assertThat(userFromDB).isNotNull().extracting("username").isNotEqualTo(newUserBoundary.getUsername());
	}
}
