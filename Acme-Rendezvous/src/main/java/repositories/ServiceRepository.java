
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.DomainService;

@Repository
public interface ServiceRepository extends JpaRepository<DomainService, Integer> {

}
