package superapp.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import superapp.restApi.boundaries.ObjectBoundary;

public interface ObjectServiceWithBindingCapabilities extends ObjectService{
	
	@Deprecated
	public void bind (String InternalObjectIdOrigin, String InternalObjectIdChildren);
	
	@Deprecated
	public List<ObjectBoundary> getAllChildren(String InternalObjectIdOrigin);
	
	public Optional<ArrayList<ObjectBoundary>> getOrigin(String InternalObjectIdChildren);

}
