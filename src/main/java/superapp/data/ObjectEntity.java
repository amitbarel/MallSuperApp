package superapp.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "SuperAppObject")
@CompoundIndexes({
    @CompoundIndex(name = "location_2dsphere", def = "{'location': '2dsphere'}")
})
public class ObjectEntity {
	@Id
	private String id;
	private String type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;
	private CreatedBy createdBy;
	@GeoSpatialIndexed
	private Location location;
	private Map<String, Object> objectDetails;
	
	@DBRef(lazy = true)
	private List<ObjectEntity> childrenObjects;
	
	@DBRef(lazy = true)
	private List<ObjectEntity> parentObjects;

//		@GeoSpatialIndexed
//		private GeoJsonPoint location;
	
//	public void setLocation(GeoJsonPoint location) {
//		this.location = location;
//	}

	public ObjectEntity() {
		this.objectDetails = new HashMap<>();
		this.childrenObjects = new ArrayList<>();
		this.parentObjects = new ArrayList<>();
	}

	public ObjectEntity(String id, String type, String alias, boolean active, Date creationTimestamp,
			CreatedBy createdBy, Location location, Map<String, Object> objectDetails,
			List<ObjectEntity> childrenObjects) {
		super();
		this.id = id;
		this.type = type;
		this.alias = alias;
		this.active = active;
		this.creationTimestamp = creationTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.objectDetails = objectDetails;
		this.childrenObjects = childrenObjects;
	}

	public String getId() {
		return id;
	}


	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	public List<ObjectEntity> getChildrenObjects() {
		return childrenObjects;
	}

	public void setChildrenObjects(List<ObjectEntity> childrenObjects) {
		this.childrenObjects = childrenObjects;
	}

	public void addChildren(ObjectEntity children) {
		this.childrenObjects.add(children);
	}
	
	public List<ObjectEntity> getParentObjects() {
		return parentObjects;
	}

//	public GeoJsonPoint getLocation() {
//		return location;
//	}
//
//
//	public void setLocation(GeoJsonPoint location) {
//		this.location = location;
//	}

	public void setParentObjects(List<ObjectEntity> parentObjects) {
		this.parentObjects = parentObjects;
	}
	
	public void addParent(ObjectEntity parent) {
		this.parentObjects.add(parent);
	}
	
	@Override
	public String toString() {
		return "ObjectEntity [id=" + id + ", type=" + type + ", alias=" + alias + ", active=" + active
				+ ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", objectDetails=" + objectDetails + ", childrenObjects=" + childrenObjects + ", parentObjects="
				+ parentObjects + "]";
	}
}
