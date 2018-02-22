
package repositories;

import java.util.Collection;

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

	@Query("select r from Rendezvous r where r.finalMode=true and r.deleted=false")
	Collection<Rendezvous> findFinalRendezvouses();

	@Query("select r from Rendezvous r where r.finalMode=true and r.deleted=false and r.adultOnly=false")
	Page<Rendezvous> findFinalWithoutAdultRendezvouses(Pageable pageable);

	@Query("select r from User u join u.createdRendezvouses r where u.id=?1")
	Page<Rendezvous> findCreatedRendezvouses(int userId, Pageable pageable);

	@Query("select r from User u join u.createdRendezvouses r where u.id=?1 and r.finalMode=true")
	Page<Rendezvous> findCreatedRendezvousesForDisplay(int userId, Pageable pageable);

	@Query("select r from Rendezvous r join r.users u where r.deleted=false and u.id=?1")
	Page<Rendezvous> findRSVPRendezvouses(int userId, Pageable pageable);

	@Query("select r from User u join u.createdRendezvouses r where u.id=?1 and r.adultOnly=false and r.finalMode=true")
	Page<Rendezvous> findCreatedRendezvousesForDisplayNotAdult(int userId, Pageable pageable);

	@Query("select r from Rendezvous r join r.users u where r.deleted=false and r.adultOnly=false and u.id=?1")
	Page<Rendezvous> findRSVPRendezvousesNotAdult(int userId, Pageable pageable);

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
	 * Level C query 5
	 * 
	 * @return The top rendezvouses in terms of users who have RSVPd them, if you want to get the top X, then you must get the first X results.
	 * @author Juanmi
	 */
	@Query("select r from Rendezvous r order by r.users.size desc")
	Page<Rendezvous> getTopRendezvouses(Pageable page);

	/**
	 * Level B query 1
	 * 
	 * @return The average and the standard deviation of announcements per rendezvous.
	 * @author Juanmi
	 */
	@Query("select avg(r.announcements.size), sqrt(sum(r.announcements.size * r.announcements.size) / count(r.announcements.size) - (avg(r.announcements.size) * avg(r.announcements.size))) from Rendezvous r")
	String getAnnouncementsInfoFromRendezvous();

	/**
	 * Level B query 2
	 * 
	 * @return The rendezvouses whose number of announcements is above 75% the average number of announcements per rendezvous.
	 * @author Juanmi
	 */
	@Query("select r from Rendezvous r where r.announcements.size > (select avg(re.announcements.size)*0.75 from Rendezvous re)")
	Collection<Rendezvous> getRendezvousesWithAnnouncementAboveSeventyFivePercent();

	/**
	 * Level B query 3
	 * 
	 * @return The rendezvouses that are linked to a number of rendezvouses that is greater than the average plus 10%.
	 * @author Juanmi
	 */
	@Query("select r from Rendezvous r where r.similars.size > (select avg(re.similars.size)+(avg(re.similars.size)*0.1) from Rendezvous re)")
	Collection<Rendezvous> getRendezvousesMostLinked();

	/**
	 * Level A query 1
	 * 
	 * @return The average and the standard deviation of the number of questions per rendezvous.
	 * @author Juanmi
	 */
	@Query("select avg(r.questions.size), sqrt(sum(r.questions.size * r.questions.size) / count(r.questions.size) - (avg(r.questions.size) * avg(r.questions.size))) from Rendezvous r")
	String getQuestionsInfoFromRendezvous();

}
