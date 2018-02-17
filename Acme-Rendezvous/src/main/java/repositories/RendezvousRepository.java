
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rendezvous;

@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {

	@Query("select r from Rendezvous r join r.questions  rq where rq.id =?1 ")
	Rendezvous findRendezvousbyQuestion(int id);

	@Query("select r from Rendezvous r join r.announcements  ra where ra.id =?1 ")
	Rendezvous findRendezvousbyAnnouncement(int id);

	@Query("select r from Rendezvous r join r.comments  rc where rc.id =?1 ")
	Rendezvous findRendezvousbyCommentary(int id);

	@Query("select r from Rendezvous r join r.similars  rs where rs.id =?1 ")
	Page<Rendezvous> findRendezvousbySimilar(int id, Pageable pageable);

	@Query("select r from Rendezvous r where r.finalMode=true and r.deleted=false")
	Page<Rendezvous> findFinalRendezvouses(Pageable pageable);

	@Query("select r from Rendezvous r where r.finalMode=true and r.deleted=false and r.adultOnly=false")
	Page<Rendezvous> findFinalWithoutAdultRendezvouses(Pageable pageable);

	@Query("select r from User u join u.createdRendezvouses r where u.id=?1")
	Page<Rendezvous> findCreatedRendezvouses(int userId, Pageable pageable);

	@Query("select r from Rendezvous r join r.users u where r.deleted=false and u.id=?1")
	Page<Rendezvous> findRSVPRendezvouses(int userId, Pageable pageable);

	// Dashboard queries.

	/**
	 * Level C query 3
	 * 
	 * @return The average and the standard deviation of users per rendezvous.
	 * @author Juanmi
	 */
	@Query("select avg(r.users.size), sqrt(sum(r.users.size * r.users.size) / count(r.users.size) - (avg(r.users.size) * avg(r.users.size))) from Rendezvous r")
	String getUsersInfoFromRendezvous();

	/**
	 * Level C query 4
	 * 
	 * @return The average and the standard deviation of rendezvouses that are RSVPd per user.
	 * @author Juanmi
	 */
	//TODO Continuar por esta query

}
