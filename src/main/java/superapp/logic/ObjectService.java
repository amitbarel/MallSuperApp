package superapp.logic;

import java.util.List;
import java.util.Optional;

import superapp.restApi.boundaries.ObjectBoundary;

public interface ObjectService {
	
	public ObjectBoundary createObject(ObjectBoundary object);

	@Deprecated
	public ObjectBoundary updateObject(String superApp, String id, ObjectBoundary ob);

	@Deprecated
	public Optional<ObjectBoundary> getSpecificObject(String superApp, String id);

	@Deprecated
	public List<ObjectBoundary> getAllObjects();
	
	@Deprecated
	public void deleteAllObjects();
	
}
