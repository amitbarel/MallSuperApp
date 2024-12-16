package superapp.restApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.data.UserId;
import superapp.logic.UsersQueries;
import superapp.restApi.boundaries.NewUserBoundary;
import superapp.restApi.boundaries.UserBoundary;

@RestController
public class UserController {

	private UsersQueries usersService;

	@Autowired
	public void setUsersService(UsersQueries usersService) {
		this.usersService = usersService;
	}

	/*
	 * Create a new user
	 */
	@RequestMapping(path = { "/superapp/users" }, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary createUserBoundary(@RequestBody NewUserBoundary nub) {
		UserBoundary ub = new UserBoundary(nub.getRole(), nub.getUsername(), nub.getAvatar(),
				new UserId(nub.getEmail()));
		return usersService.createUser(ub);
	}

	/*
	 * Login valid user and retrieve user details
	 */
	@RequestMapping(path = { "/superapp/users/login/{superapp}/{email}" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary getUserBoundaryByEmail(@PathVariable("superapp") String superapp,
			@PathVariable("email") String email) {
		return this.usersService.login(superapp, email);
	}

	@RequestMapping( // Update the details
			path = { "/superapp/users/{superapp}/{userEmail}" }, method = { RequestMethod.PUT }, consumes = {
					MediaType.APPLICATION_JSON_VALUE })
	public void update(@PathVariable("superapp") String superapp, @PathVariable("userEmail") String email,
			@RequestBody UserBoundary update) {
		this.usersService.updateUser(superapp, email, update);
	}

}
