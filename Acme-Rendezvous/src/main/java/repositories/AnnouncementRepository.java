
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Announcement;
import domain.Rendezvous;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

	
	
	//@Query("select r from Rendezvous r join r.similars  rs where rs.id =?1 ")
	//Page<Rendezvous> medianRendezvousAnnouncement();
	
	//@Query("select r from Rendezvous r join r.similars  rs where rs.id =?1 ")
	//Page<Rendezvous> dvRendezvousAnnouncement();
	
	//@Query("select r from Rendezvous r join r.similars  rs where rs.id =?1 ")
	//Page<Rendezvous> percentageRendezvousAnnouncement();
	
	
}
