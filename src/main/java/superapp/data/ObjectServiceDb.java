package superapp.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import superapp.logic.ObjectCrud;
import superapp.logic.ObjectQueries;
import superapp.logic.UserCrud;
import superapp.restApi.boundaries.ObjectBoundary;
import superapp.data.exceptions.ForbiddenException;
import superapp.data.exceptions.ObjectNotFoundException;
import superapp.data.exceptions.UserNotFoundException;
import java.util.ArrayList;

@Service
public class ObjectServiceDb implements ObjectQueries {
	private ObjectCrud objectCrud;
	private UserCrud userCrud;
	private String superapp;
	private String delimeter = "_";

	@Autowired
	public void setObjectCrud(ObjectCrud objectCrud) {
		this.objectCrud = objectCrud;
	}

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Value("${spring.application.name:2023b.shir.zur}")
	public void setSuperapp(String name) {
		this.superapp = name;
	}

	private ObjectBoundary toBoundary(ObjectEntity entity) {
		ObjectBoundary ob = new ObjectBoundary();
		String a = entity.getId();
		String[] parts = a.split(delimeter);
		String part1 = parts[0]; // superapp name
		String part2 = parts[1]; // internal object id
		ob.setObjectId(new ObjectId().setInternalObjectId(part2).setSuperapp(part1));
		ob.setType(entity.getType());
		ob.setAlias(entity.getAlias());
		ob.setActive(entity.getActive());
		ob.setCreationTimestamp(entity.getCreationTimestamp());
//		 ob.setLocation(new
//		 Location().setLat(entity.getLat()).setLng(entity.getLng()));
//		ob.setLocation(new Location(entity.getLocation().getX(), entity.getLocation().getY()));
		ob.setLocation(new Location(entity.getLocation().getLat(), entity.getLocation().getLng()));
		ob.setCreatedBy(entity.getCreatedBy());
		ob.setObjectDetails(entity.getObjectDetails());
		return ob;
	}

	private ObjectEntity toEntity(ObjectBoundary object) throws ObjectNotFoundException, UserNotFoundException {
		if (object == null) {
			throw new ObjectNotFoundException("Object can not be null");
		}
		ObjectEntity entity = new ObjectEntity();

		entity.setId(superapp + delimeter + object.getObjectId().getInternalObjectId());
		if (object.getType() != null && !object.getType().replaceAll(" ", "").isEmpty()) {
			entity.setType(object.getType());
		} else {
			throw new ObjectNotFoundException("Type can not be null or empty");
		}
		if (object.getAlias() != null && !object.getAlias().replaceAll(" ", "").isEmpty()) {
			entity.setAlias(object.getAlias());
		} else {
			throw new ObjectNotFoundException("Alias can not be null or empty");
		}
		if (object.getActive() != null) {
			entity.setActive(object.getActive());
		} else {
			entity.setActive(true);
		}
		if (object.getLocation() != null) {
//			entity.setLat(object.getLocation().getLat());
//			entity.setLng(object.getLocation().getLng());

			entity.setLocation(
//					new GeoJsonPoint(new Point(object.getLocation().getLat(), object.getLocation().getLng())));
					new Location(object.getLocation().getLat(), object.getLocation().getLng()));
		} else {
//			entity.setLat((double) 0);
//			entity.setLng((double) 0);
//			entity.setLocation(new GeoJsonPoint(new Point(0, 0)));
			entity.setLocation(new Location(0,0));
		}
		if (object.getCreatedBy() != null) {
			if (object.getCreatedBy().getUserId() != null) {
				if (object.getCreatedBy().getUserId().getEmail() != null) {
					entity.setCreatedBy(object.getCreatedBy());
					entity.getCreatedBy().getUserId().setSuperapp(superapp);
				} else {
					throw new UserNotFoundException("The email entered is not a valid email");
				}
			} else {
				throw new UserNotFoundException("The id entered is not a valid id");
			}
		} else {
			throw new UserNotFoundException("The user that the object has been created by is not a valid user");
		}
		if (object.getObjectDetails() != null) {
			entity.setObjectDetails(object.getObjectDetails());
		} else {
			entity.setObjectDetails(new HashMap<>());
		}
		return entity;
	}

	@Override
	public ObjectBoundary createObject(ObjectBoundary object) {
		String userSuperapp = object.getCreatedBy().getUserId().getSuperapp();
		String userEmail = object.getCreatedBy().getUserId().getEmail();

		UserEntity userEntity = this.userCrud.findById(userSuperapp + delimeter + userEmail)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + userSuperapp + " and email = " + userEmail));
		if (userEntity.getRole() != UserRole.SUPERAPP_USER) {
			throw new ForbiddenException("User not allowed to make this action");
		} else {
			object.setObjectId(new ObjectId().setInternalObjectId(UUID.randomUUID().toString()));
			object.getObjectId().setSuperapp(superapp);
			ObjectEntity entity = (ObjectEntity) toEntity(object);
			if (object.getLocation() != null)
				entity.setLocation(
//						new GeoJsonPoint(new Point(object.getLocation().getLat(), object.getLocation().getLng())));
						new Location(object.getLocation().getLat(), object.getLocation().getLng()));

			else {
//				entity.setLocation(new GeoJsonPoint(new Point(0, 0)));
				entity.setLocation(new Location(0,0));
			}
			entity.setCreationTimestamp(new Date());
			entity = this.objectCrud.save(entity);
			return (ObjectBoundary) toBoundary(entity);

		}
	}

	@Deprecated
	@Override
	public ObjectBoundary updateObject(String superApp, String id, ObjectBoundary ob) {
		ObjectEntity existing = this.objectCrud.findById(superApp + delimeter + id).orElseThrow(
				() -> new ObjectNotFoundException("Could not find an object to update by id: " + (superApp + id)));
		if (ob.getType() != null) {
			existing.setType(ob.getType());
		}
		if (ob.getAlias() != null) {
			existing.setAlias(ob.getAlias());
		}
		if (ob.getActive() != null) {
			existing.setActive(ob.getActive());
		}
		if (ob.getLocation() != null) {
//			existing.setLat(ob.getLocation().getLat());
//			existing.setLng(ob.getLocation().getLng());
			existing.setLocation//(new GeoJsonPoint(new Point(ob.getLocation().getLat(), ob.getLocation().getLng())));
			(new Location(ob.getLocation().getLat(), ob.getLocation().getLng()));

		}
		if (ob.getObjectDetails() != null) {
			existing.setObjectDetails(ob.getObjectDetails());
		}
		existing = this.objectCrud.save(existing);
		return (ObjectBoundary) this.toBoundary(existing);
	}

	@Deprecated
	@Override
	public Optional<ObjectBoundary> getSpecificObject(String superApp, String id) {
		return this.objectCrud.findById(superApp + delimeter + id).map(this::toBoundary);
	}

	@Deprecated
	@Override
	public List<ObjectBoundary> getAllObjects() {
		return this.objectCrud.findAll().stream() // Stream<ObjectEntity>
				.map(this::toBoundary) // Stream<ObjectBound>
				.toList(); // List<Message>
	}

	@Deprecated
	@Override
	public void deleteAllObjects() {
		this.objectCrud.deleteAll();
	}

	@Deprecated
	@Override
	public void bind(String InternalObjectIdOrigin, String InternalObjectIdChildren) {
		ObjectEntity origin = this.objectCrud.findById(superapp + delimeter + InternalObjectIdOrigin)
				.orElseThrow(() -> new ObjectNotFoundException(
						"Could not find the origin object by the given id: " + InternalObjectIdOrigin));

		ObjectEntity children = this.objectCrud.findById(superapp + delimeter + InternalObjectIdChildren)
				.orElseThrow(() -> new ObjectNotFoundException(
						"Could not find the child object by id: " + InternalObjectIdChildren));
		if (origin.getId().equals(children.getId()))
			throw new ObjectNotFoundException("The origin Id and children Id can not be the same");

		origin.addChildren(children);

		this.objectCrud.save(origin);

	}

	@Deprecated
	@Override
	public List<ObjectBoundary> getAllChildren(String InternalObjectIdOrigin) {
		ObjectEntity origin = this.objectCrud.findById(superapp + delimeter + InternalObjectIdOrigin).orElseThrow(
				() -> new ObjectNotFoundException("Could not find origin Object by id: " + InternalObjectIdOrigin));

		List<ObjectEntity> ChildrenObjects = origin.getChildrenObjects();

		List<ObjectBoundary> rv = new ArrayList<>();
		for (ObjectEntity entity : ChildrenObjects) {
			rv.add(this.toBoundary(entity));
		}
		return rv;
	}

	@Override
	public Optional<ArrayList<ObjectBoundary>> getOrigin(String InternalObjectIdChildren) {
		ObjectEntity children = this.objectCrud.findById(superapp + delimeter + InternalObjectIdChildren).orElseThrow(
				() -> new ObjectNotFoundException("Could not find child Object by id: " + InternalObjectIdChildren));

		Optional<ArrayList<ObjectEntity>> originOptional = this.objectCrud.findAllByChildrenObjectsContains(children);
		return originOptional.map(list -> {
			ArrayList<ObjectBoundary> resultList = new ArrayList<>();
			for (ObjectEntity entity : list) {
				resultList.add(toBoundary(entity));
			}
			return resultList;
		});
	}

	@Override
	public void updateObjectCheckingRole(String superApp, String internalObjectId, ObjectBoundary ob,
			String userSuperapp, String email) {
		ObjectEntity existing = this.objectCrud.findById(superApp + delimeter + internalObjectId)
				.orElseThrow(() -> new ObjectNotFoundException(
						"Could not find object for update by id: " + (superApp + internalObjectId)));
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + superapp + " and email = " + email));
		if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
			if (ob.getActive() != null) {
				existing.setActive(ob.getActive());
			}
			if (ob.getType() != null && !ob.getType().isEmpty()) {
				existing.setType(ob.getType());
			}
			if (ob.getAlias() != null && !ob.getAlias().isEmpty()) {
				existing.setAlias(ob.getAlias());
			}
			if (ob.getLocation() != null) {
//				existing.setLat(ob.getLocation().getLat());
//				existing.setLng(ob.getLocation().getLng());
				existing.setLocation(//new GeoJsonPoint(new Point(ob.getLocation().getLat(), ob.getLocation().getLng())));
						new Location(ob.getLocation().getLat(), ob.getLocation().getLng()));

			}
			if (ob.getObjectDetails() != null) {
				existing.setObjectDetails(ob.getObjectDetails());
			}
			existing = this.objectCrud.save(existing);
		} else
			throw new ForbiddenException("This user does not have permission to do this");
	}

	@Override
	public Optional<ObjectBoundary> getObjectCheckingRole(String superApp, String internalObjectId, String userSuperapp,
			String email) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + superapp + " and email = " + email));

		ObjectEntity objectEntity = this.objectCrud.findById(superApp + delimeter + internalObjectId)
				.orElseThrow(() -> new ObjectNotFoundException(
						"Could not find object for update by id: " + (superApp + internalObjectId)));

		if (objectEntity.getActive() == false) {
			if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
				return Optional.of(toBoundary(objectEntity));
			} else if (userEntity.getRole() == UserRole.MINIAPP_USER) {
				throw new ObjectNotFoundException("Object is not found");
			} else {
				throw new ForbiddenException("This user does not have permission to do this");
			}
		}
		return Optional.of(toBoundary(objectEntity));
	}

	@Override
	public List<ObjectBoundary> getAllObjectsCheckingRole(String userSuperapp, String email, int size, int page) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + superapp + " and email = " + email));

		if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
			return this.objectCrud.findAll(PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "id"))
					.stream().map(this::toBoundary).toList();
		} else if (userEntity.getRole() == UserRole.MINIAPP_USER) {
			return this.objectCrud
					.findAllByActiveIsTrue(PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "id"))
					.stream().map(this::toBoundary).toList();
		}
		throw new ForbiddenException("This user does not have permission to do this");
	}

	@Override
	public void deleteAllObjectsAdminOnly(String superapp, String email) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + superapp + " and email = " + email));
		if (userEntity.getRole() == UserRole.ADMIN)
			this.objectCrud.deleteAll();
		else
			throw new ForbiddenException("This user does not have permission to do this");

	}

	@Override
	public void bindByPermission(String originId, ObjectId childrenObjectId,
			String userSuperapp, String email) {
		String childrenId = childrenObjectId.getInternalObjectId();
		ObjectEntity origin = this.objectCrud.findById(superapp + delimeter + originId)
				.orElseThrow(() -> new ObjectNotFoundException("could not find origin Object by id: " + originId));
		UserEntity userEntity = this.userCrud.findById(userSuperapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + userSuperapp + " and email = " + email));
		if (userEntity.getRole() != UserRole.SUPERAPP_USER) {
			throw new ForbiddenException("This user has no permission binding these objects");
		} else {
			ObjectEntity children = this.objectCrud.findById(superapp + delimeter + childrenId)
					.orElseThrow(() -> new ObjectNotFoundException("Could not find child Object by id: " + childrenId));
			if (origin.getId().equals(children.getId()))
				throw new ObjectNotFoundException("The origin ID and children ID can not be same");
			for (ObjectEntity objectEntity : origin.getChildrenObjects()) {
				if(objectEntity.getId().equals(children.getId()))
					throw new ObjectNotFoundException("The childern already exist!");
			}
			origin.addChildren(children);
			children.addParent(origin);

			this.objectCrud.save(origin);
			this.objectCrud.save(children);
		}
	}

	@Override
	public List<ObjectBoundary> getAllChildrenByPermission(String superApp, String originId, String userSuperapp,
			String email, int size, int page) {
		UserEntity userEntity = this.userCrud.findById(userSuperapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + userSuperapp + " and email = " + email));

		ObjectEntity origin = this.objectCrud.findById(superApp + delimeter + originId)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find origin Object by id: " + originId));

		if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
			return objectCrud
					.findAllByParentObjectsIsContaining(origin, PageRequest.of(page, size, Direction.DESC, "id"))
					.stream().map(this::toBoundary).toList();
		} else if (userEntity.getRole() == UserRole.MINIAPP_USER) {
			if (origin.getActive() == false)
				return new ArrayList<>();
			else {
				return this.objectCrud
						.findAllByParentObjectsIsContainingAndActiveIsTrue(origin,
								PageRequest.of(page, size, Direction.DESC, "id"))
						.stream().map(this::toBoundary).toList();
			}
		}
		throw new ObjectNotFoundException("Object not found");
	}

	@Override
	public List<ObjectBoundary> getAllParentsByPermission(String superApp, String childrenId, String userSuperapp,
			String email, int size, int page) {
		UserEntity userEntity = this.userCrud.findById(userSuperapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + userSuperapp + " and email = " + email));

		ObjectEntity children = this.objectCrud.findById(superApp + delimeter + childrenId)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find children Object by id: " + childrenId));

		if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
			return objectCrud
					.findAllByChildrenObjectsIsContaining(children, PageRequest.of(page, size, Direction.DESC, "id"))
					.stream().map(this::toBoundary).toList();
		} else if (userEntity.getRole() == UserRole.MINIAPP_USER) {
			if (children.getActive() == false)
				return new ArrayList<>();
			else {
				return this.objectCrud
						.findAllByChildrenObjectsIsContainingAndActiveIsTrue(children,
								PageRequest.of(page, size, Direction.DESC, "id"))
						.stream().map(this::toBoundary).toList();
			}
		}
		throw new ObjectNotFoundException("Object not found");
	}

	@Override
	public List<ObjectBoundary> getObjectsByType(String superapp, String email, String type, int size, int page) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + superapp + " and email = " + email));
		if (userEntity.getRole() == UserRole.MINIAPP_USER) {
			return this.objectCrud
					.findAllByTypeAndActiveIsTrue(type, PageRequest.of(page, size, Direction.DESC, "type", "id"))
					.stream().map(this::toBoundary).toList();
		} else if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
			return this.objectCrud.findAllByType(type, PageRequest.of(page, size, Direction.DESC, "type", "id"))
					.stream().map(this::toBoundary).toList();
		} else {
			throw new ForbiddenException("This user does not have permission to do this");
		}
	}

	@Override
	public List<ObjectBoundary> getObjectsByAlias(String superapp, String email, String alias, int size, int page) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + superapp + " and email = " + email));
		if (userEntity.getRole() == UserRole.MINIAPP_USER) {
			return this.objectCrud
					.findAllByAliasAndActiveIsTrue(alias, PageRequest.of(page, size, Direction.DESC, "alias", "id"))
					.stream().map(this::toBoundary).toList();
		} else if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
			return this.objectCrud.findAllByAlias(alias, PageRequest.of(page, size, Direction.DESC, "alias", "id"))
					.stream().map(this::toBoundary).toList();
		} else {
			throw new ForbiddenException("This user does not have permission to do this");
		}
	}

	@Override
	public List<ObjectBoundary> getObjectsByLocation(String superapp, String email, double lat, double lng,
			double distance, String distanceUnits, int size, int page) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + superapp + " and email = " + email));
		
		Point point = new Point(lat,lng);
		Metrics distanceType;
		
		switch (distanceUnits) {
		case "NEUTRAL":
			 distanceType = Metrics.NEUTRAL;
			break;
		case "KILOMETERS":
			 distanceType = Metrics.KILOMETERS;
			distance *= 1000;
			break;
		case "MILES":
			 distanceType = Metrics.MILES;
			distance *= 1609.344;
			break;
		default:
			 distanceType = Metrics.NEUTRAL;

			break;
		}
		
		Distance radiusDistance = new Distance(distance, distanceType);
		if (userEntity.getRole() == UserRole.MINIAPP_USER) {
			return this.objectCrud.findByActiveIsTrueAndLocationNear(
					point, 
					radiusDistance, 
					PageRequest.of(page, size, Direction.DESC, "id"))
					.stream().map(this::toBoundary).toList();
		} else if (userEntity.getRole() == UserRole.SUPERAPP_USER) {
			return this.objectCrud.findByLocationNear(
					point, 
					radiusDistance, 
					PageRequest.of(page, size, Direction.DESC, "id"))
					.stream().map(this::toBoundary).toList();
		} else {
			throw new ForbiddenException("This user does not have permission to do this");
		}
	}

}
