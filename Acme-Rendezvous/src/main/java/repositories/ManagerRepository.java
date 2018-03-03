
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DomainService;
import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m.services from Manager m where m.id=?1")
	Page<DomainService> findServicesByManager(int managerId, Pageable pageable);
}
