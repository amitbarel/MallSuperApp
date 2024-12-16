package superapp.restApi;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import superapp.logic.*;
import superapp.restApi.boundaries.MiniAppCommandBoundary;
import superapp.restApi.boundaries.UserBoundary;

@RestController
public class AdminUserController {

	private final String DEFAULT_PAGE_SIZE = "3";
	private final String DEFAULT_PAGE_NUM = "0";
	private UsersQueries usersService;
	private ObjectQueries objectService;
	private MiniAppCommandsQueries miniAppCommandsService;

	@Autowired
	public void setUsersService(UsersQueries usersService) {
		this.usersService = usersService;
	}

	@Autowired
	public void setObjectService(ObjectQueries objectService) {
		this.objectService = objectService;
	}

	@Autowired
	public void setMiniAppCommandsService(MiniAppCommandsQueries miniAppCommandsService) {
		this.miniAppCommandsService = miniAppCommandsService;
	}

	@RequestMapping(path = { "/superapp/admin/users" }, method = { RequestMethod.DELETE })
	public void deleteAllUsers(@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		this.usersService.deleteAllUsersAdminOnly(superapp, email);
	}

	@RequestMapping(path = { "/superapp/admin/objects" }, method = { RequestMethod.DELETE })
	public void deleteAllObjects(@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		this.objectService.deleteAllObjectsAdminOnly(superapp, email);
	}

	@RequestMapping(path = { "/superapp/admin/miniapp" }, method = { RequestMethod.DELETE })
	public void deleteAllCommandsHistory(@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		this.miniAppCommandsService.deleteAllCommandsAdminOnly(superapp, email);
	}

	@RequestMapping(path = { "/superapp/admin/users" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<UserBoundary> exportAllUsers(@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		return usersService.getAllUsersAdminOnly(superapp,email,size,page);
	}

	@RequestMapping(path = { "/superapp/admin/miniapp" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<MiniAppCommandBoundary> exportAllCommandsHistory(
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		return miniAppCommandsService.getAllMiniAppsCommandsAdminOnly(superapp, email, size, page);
	}

	@RequestMapping(path = { "/superapp/admin/miniapp/{miniAppName}" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<MiniAppCommandBoundary> exportSpecificMiniAppCommandHistory(@PathVariable String miniAppName,
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		return miniAppCommandsService.getSpecificMiniAppCommandsAdminOnly(miniAppName, superapp, email, size, page);
	}

}
