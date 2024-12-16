package superapp.restApi.boundaries;

import superapp.data.ObjectId;

public class TargetObjectBoundary {
	
	private ObjectId objectId;
	
	public  TargetObjectBoundary() {
	}

	public TargetObjectBoundary(ObjectId objectId) {
		super();
		this.objectId = objectId;
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	@Override
	public String toString() {
		return "TargetObjectBoundary [objectId=" + objectId + "]";
	}
}
