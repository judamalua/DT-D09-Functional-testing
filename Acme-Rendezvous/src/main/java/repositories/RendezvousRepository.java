
package repositories;

import java.util.Collection;

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

	@Query("select r from Rendezvous r join r.commentaries  rc where rc.id =?1 ")
	Rendezvous findRendezvousbyCommentary(int id);

	@Query("select r from Rendezvous r join r.similars  rs where rs.id =?1 ")
	Collection<Rendezvous> findRendezvousbySimilar(int id);

}
