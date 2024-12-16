package superapp.restApi;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import superapp.logic.ObjectQueries;
import superapp.restApi.boundaries.ObjectBoundary;

@RestController
public class ObjectController {

	private final String DEFAULT_PAGE_SIZE = "3";
	private final String DEFAULT_PAGE_NUM = "0";
	private ObjectQueries objectService;

	@Autowired
	public ObjectController(ObjectQueries objectService) {
		this.objectService = objectService;
	}

	@RequestMapping( // Create an object
			path = { "/superapp/objects" }, method = { RequestMethod.POST }, produces = {
					MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary createObjectBoundary(@RequestBody ObjectBoundary ob) {
		return this.objectService.createObject(ob);
	}

	@RequestMapping( // Update an object
			path = { "/superapp/objects/{superApp}/{internalObjectId}" }, method = { RequestMethod.PUT }, consumes = {
					MediaType.APPLICATION_JSON_VALUE })
	public void updateObject(@PathVariable("superApp") String superApp, @PathVariable("internalObjectId") String id,
			@RequestBody ObjectBoundary ob, @RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		objectService.updateObjectCheckingRole(superApp, id, ob, userSuperapp, email);
	}

	@RequestMapping( // Retrieve object
			path = { "/superapp/objects/{superApp}/{internalObjectId}" }, method = { RequestMethod.GET }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Optional<ObjectBoundary> getSpecificObject(@PathVariable("superApp") String superApp,
			@PathVariable("internalObjectId") String id,
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		return this.objectService.getObjectCheckingRole(superApp, id, superapp, email);
	}

	@RequestMapping( // Get All objects
			path = { "/superapp/objects" }, method = { RequestMethod.GET }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getAllObjects(@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		return objectService.getAllObjectsCheckingRole(superapp, email, size, page).toArray(new ObjectBoundary[0]);
	}

	@RequestMapping( // Get All objects
			path = { "/superapp/objects/search/byType/{type}" }, method = { RequestMethod.GET }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectsByType(@PathVariable("type") String type,
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		return objectService.getObjectsByType(superapp, email, type, size, page).toArray(new ObjectBoundary[0]);
	}

	@RequestMapping( // Get All objects
			path = { "/superapp/objects/search/byAlias/{alias}" }, method = { RequestMethod.GET }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectsByAlias(@PathVariable("alias") String alias,
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		return objectService.getObjectsByAlias(superapp, email, alias, size, page).toArray(new ObjectBoundary[0]);
	}

	@RequestMapping( // Get All objects
			path = { "/superapp/objects/search/byLocation/{lat}/{lng}/{distance}" }, method = {
					RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectsByLocation(@PathVariable("lat") Double lat, @PathVariable("lng") Double lng,
			@PathVariable("distance") double distance,
			@RequestParam(name = "units", required = false, defaultValue = "NEUTRAL") String distanceUnits,
			@RequestParam(name = "userSuperapp", required = true) String superapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		return objectService.getObjectsByLocation(superapp, email, lat, lng, distance, distanceUnits, size, page)
				.toArray(new ObjectBoundary[0]);
	}

}