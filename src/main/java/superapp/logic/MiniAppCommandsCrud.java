package superapp.logic;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import superapp.data.MiniAppCommandEntity;

public interface MiniAppCommandsCrud
		extends ListCrudRepository<MiniAppCommandEntity, String>, PagingAndSortingRepository<MiniAppCommandEntity, String> {

	public List<MiniAppCommandEntity> findAllByInvokedByUserIdEmail(@Param("email") String email, Pageable pageable);

	public List<MiniAppCommandEntity> findAllByMiniApp(@Param("miniApp") String miniApp, Pageable pageable);

}
