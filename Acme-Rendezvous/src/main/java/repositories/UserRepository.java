
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u join u.RCVPRendezvouses ur where us.id =?1 ")
	Collection<User> findUsersRSVPs(int id);

	@Query("select u from User u join u.createdRendezvouses cr where cr.id =?1 ")
	User findCreatoUser(int id);

	@Query("select u from User u join u.answers ua where ua.id =?1 ")
	User getUserFromAnswerId(int id);

}
