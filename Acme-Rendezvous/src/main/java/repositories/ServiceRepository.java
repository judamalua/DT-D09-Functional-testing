
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;
import domain.DomainService;

@Repository
public interface ServiceRepository extends JpaRepository<DomainService, Integer> {

	@Query("select s from DomainService s where s.cancelled=false")
	Page<DomainService> findNotCancelledServices(Pageable pageable);

	@Query("select s.categories from DomainService s where s.id=?1")
	Page<Category> findCategoriesByService(int serviceId, Pageable pageable);

	//Dashboard queries

	@Query("select s from DomainService s where s.requests.size=(select max(sv.requests.size) from DomainService sv))")
	Collection<DomainService> findBestSellingServices();

}
