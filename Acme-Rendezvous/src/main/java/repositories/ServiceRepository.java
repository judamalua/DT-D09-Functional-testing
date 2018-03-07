
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DomainService;

@Repository
public interface ServiceRepository extends JpaRepository<DomainService, Integer> {

	@Query("select s from DomainService s where s.cancelled=false")
	Page<DomainService> findNotCancelledServices(Pageable pageable);

	@Query("select req.service from Rendezvous r join r.requests req where r.id = ?1")
	Collection<DomainService> getServicesRequestedFromRendezvous(int rendezvousId);

	//Dashboard queries

	@Query("select s from DomainService s where s.requests.size=(select max(sv.requests.size) from DomainService sv))")
	Collection<DomainService> findBestSellingServices();

}
