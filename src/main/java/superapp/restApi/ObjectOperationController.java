package superapp.restApi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.data.ObjectId;
import superapp.logic.ObjectQueries;
import superapp.restApi.boundaries.ObjectBoundary;

@RestController
public class ObjectOperationController {
	
	private final String DEFAULT_PAGE_SIZE = "3";
	private final String DEFAULT_PAGE_NUM = "0";
	private ObjectQueries objectService;

	@Autowired
	public ObjectOperationController(ObjectQueries objectService) {
		super();
		this.objectService = objectService;
	}

	@RequestMapping(method = {
			RequestMethod.PUT }, path = "/superapp/objects/{superApp}/{InternalObjectId}/children", consumes = {
					MediaType.APPLICATION_JSON_VALUE })
	public void bindExistingObjectToChildObject(@PathVariable("superApp") String superApp,
			@PathVariable("InternalObjectId") String originId,
			@RequestBody ObjectId superAppObjectIdBoundary,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		this.objectService.bindByPermission(originId, superAppObjectIdBoundary, userSuperapp, email);
	}

	@RequestMapping(method = {
			RequestMethod.GET }, path = "/superapp/objects/{superApp}/{InternalObjectId}/children", produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getAllChildrenOfObject(@PathVariable("superApp") String superApp,
			@PathVariable("InternalObjectId") String originId,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		List<ObjectBoundary> rv = this.objectService.getAllChildrenByPermission(superApp, originId, userSuperapp, email,
				size, page);
		return rv.toArray(new ObjectBoundary[0]);
	}

	@RequestMapping(method = {
			RequestMethod.GET }, path = "/superapp/objects/{superApp}/{InternalObjectId}/parents", produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getAnArrayWithObjectParent(@PathVariable("superApp") String superApp,
			@PathVariable("InternalObjectId") String childrenId,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUM) int page) {
		List<ObjectBoundary> rv = this.objectService.getAllParentsByPermission(superApp, childrenId, userSuperapp,
				email, size, page);
		return rv.toArray(new ObjectBoundary[0]);
	}
}
