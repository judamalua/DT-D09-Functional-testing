
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DomainService;
import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select s from Manager m join m.services s where m.id=?1")
	Page<DomainService> findServicesByManager(int managerId, Pageable pageable);

	@Query("select m from Manager m join m.services s where s.id=?1")
	Manager findManagerByService(int serviceId);

	//Dashboard configuration

	@Query("select m from Manager m  where m.services.size>(select avg(mn.services.size) from Manager mn))")
	Collection<Manager> findBestProviderThanAverageManager();

	@Query("select m from Manager m join m.services s where s.cancelled=true")
	Collection<Manager> findMoreCancelledServicesManager();
}
