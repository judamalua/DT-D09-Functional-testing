
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u join u.createdRendezvouses cr where cr.id =?1 ")
	User findCreatoUser(int id);

	// Dashboard queries.

	/**
	 * Level C query 1
	 * 
	 * @return The average and the standard deviation of rendezvouses created per user.
	 * @author Juanmi
	 */
	@Query("select avg(u.createdRendezvouses.size), sqrt(sum(u.createdRendezvouses.size * u.createdRendezvouses.size) / count(u.createdRendezvouses.size) - (avg(u.createdRendezvouses.size) * avg(u.createdRendezvouses.size))) from User u")
	String getRendezvousesInfoFromUsers();

	/**
	 * Level C query 2
	 * 
	 * @return The ratio of users who have ever created a rendezvous versus the users who have never created any rendezvouses.
	 * @author Juanmi
	 */
	@Query("select sum(case when(u.createdRendezvouses.size>0) then 1.0 else 0.0 end)/count(u) from User u")
	String getRatioCreatedRendezvouses();

	/**
	 * Level C query 4
	 * 
	 * @return The average and the standard deviation of rendezvouses that are RSVPd per user.
	 * @author Juanmi
	 */
	@Query("select avg(u.rsvpRendezvouses.size), sqrt(sum(u.rsvpRendezvouses.size * u.rsvpRendezvouses.size) / count(u.rsvpRendezvouses.size) - (avg(u.rsvpRendezvouses.size) * avg(u.rsvpRendezvouses.size))) from User u")
	String getRSVPedInfoFromRendezvous();

}
