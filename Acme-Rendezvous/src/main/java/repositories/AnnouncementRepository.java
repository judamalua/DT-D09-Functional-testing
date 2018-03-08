
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

	@Query("select a from User u join u.createdRendezvouses r join r.announcements a where u.id=?1 order by a.moment DESC")
	Collection<Announcement> findCreatedAnnouncementByUserOrderByMoment(int userId);
}
