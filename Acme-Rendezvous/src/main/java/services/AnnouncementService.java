
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AnnouncementRepository;
import security.Authority;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class AnnouncementService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AnnouncementRepository	announcementRepository;
	@Autowired
	private RendezvousService rendezvousService;
	@Autowired
	private UserService userService;
	@Autowired
	private ActorService actorService;



	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Announcement create() {
		Announcement result;

		result = new Announcement();

		return result;
	}

	public Collection<Announcement> findAll() {

		Collection<Announcement> result;

		Assert.notNull(this.announcementRepository);
		result = this.announcementRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Announcement findOne(final int announcementId) {

		Announcement result;

		result = this.announcementRepository.findOne(announcementId);

		return result;

	}

	public Announcement save(final Announcement announcement) {

		assert announcement != null;

		if (announcement.getVersion() == 0){
			//The announcement moment is actual when the announcement is created 
			announcement.setMoment(new Date(System.currentTimeMillis() + 10));
			Rendezvous rend = rendezvousService.getRendezvousByAnnouncement(announcement.getId());
			rend.getAnnouncements().add(announcement);
			rendezvousService.save(rend);
		}
		Announcement result;

		result = this.announcementRepository.save(announcement);

		return result;

	}

	public void delete(final Announcement announcement) {

		assert announcement != null;
		assert announcement.getId() != 0;
		
		Assert.isTrue(this.announcementRepository.exists(announcement.getId()));

		
		//Checkear que el usuario es el creador o administrador
		Rendezvous rend = rendezvousService.getRendezvousByAnnouncement(announcement.getId());
		User user = userService.getCreatorUser(rend.getId());
		Assert.isTrue(actorService.findActorByPrincipal().getUserAccount().getAuthorities().contains(Authority.ADMIN) 
				|| user.equals(actorService.findActorByPrincipal()));
		

		this.announcementRepository.delete(announcement);

	}
}
