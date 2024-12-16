package superapp.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import superapp.data.ObjectEntity;

public interface ObjectCrud
		extends ListCrudRepository<ObjectEntity, String>, PagingAndSortingRepository<ObjectEntity, String> {

	public Optional<ArrayList<ObjectEntity>> findAllByChildrenObjectsContains(ObjectEntity theChildrenObjects);

	public List<ObjectEntity> findAllByCreatedByUserIdEmail(@Param("superapp") String userSuperapp,
			@Param("email") String email, Pageable pageable);

	public void deleteByCreatedByUserIdEmail(@Param("superapp") String superapp, @Param("email") String email);

	public List<ObjectEntity> findAllByActiveIsTrue(Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndActiveIsTrue(String type,Pageable pageable);

	public List<ObjectEntity> findAllByType(String type, Pageable pageable);

	public List<ObjectEntity> findAllByAliasAndActiveIsTrue(String alias, Pageable pageable);

	public List<ObjectEntity> findAllByAlias(String alias, Pageable pageable);

	public List<ObjectEntity> findAllByParentObjectsIsContainingAndActiveIsTrue(ObjectEntity parent, Pageable pageable);

	public List<ObjectEntity> findAllByParentObjectsIsContaining(ObjectEntity origin, Pageable pageable);

	public List<ObjectEntity> findAllByChildrenObjectsIsContaining(ObjectEntity children, Pageable pageable);
	
	public List<ObjectEntity> findAllByChildrenObjectsIsContainingAndActiveIsTrue(ObjectEntity parent, Pageable pageable);

	public List<ObjectEntity> findByLocationNear(@Param("location") Point location,@Param("maxDistance") Distance maxDistance,Pageable pageable);
	
	public List<ObjectEntity> findByActiveIsTrueAndLocationNear(@Param("location") Point location,@Param("maxDistance") Distance maxDistance,Pageable pageable);
	
}
