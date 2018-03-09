
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;
import domain.User;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r.requests from User u join u.createdRendezvouses r where u = ?1")
	Collection<Request> getAllRequestFromUserPrincipal(User user);

	@Query("select r from Request r where r.creditCard.id = ?1")
	Collection<Request> getAllRequestFromCreditCard(int creditCardId);
}
