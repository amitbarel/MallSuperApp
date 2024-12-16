package superapp.logic;

import java.util.List;

import superapp.restApi.boundaries.UserBoundary;

public interface UsersQueries extends UsersService {

	public void deleteAllUsersAdminOnly(String superapp, String email);
	
	public List<UserBoundary> getAllUsersAdminOnly(String superapp, String email, int size, int page);
	
}
