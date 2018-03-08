
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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
	private RendezvousService		rendezvousService;
	@Autowired
	private UserService				userService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private Validator				validator;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------
	/**
	 * Create a new entity of an Announcement
	 * 
	 * @return a entity of an Announcement
	 * @author Alejandro
	 */
	public Announcement create() {
		Announcement result;

		result = new Announcement();

		return result;
	}

	/**
	 * Get all the Announcement in the system
	 * 
	 * @return the list of all Announcements in the system
	 * @author Alejandro
	 */
	public Collection<Announcement> findAll() {

		Collection<Announcement> result;

		Assert.notNull(this.announcementRepository);
		result = this.announcementRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * 
	 * Get the Announcement with id announcementId
	 * 
	 * @param announcementId
	 * 
	 * @return an Announcement with id equals to announcementId
	 * @author Alejandro
	 */
	public Announcement findOne(final int announcementId) {

		Announcement result;

		result = this.announcementRepository.findOne(announcementId);

		return result;

	}

	/**
	 * Save the param announcement into the Rendezvous with id rendezvousId
	 * 
	 * @param announcement
	 * @param rendezvousId
	 * 
	 * @return the saved entity of announcement
	 * @author Alejandro
	 */
	public Announcement save(final Announcement announcement, final Integer rendezvousId) {

		assert announcement != null;
		Announcement result;
		Rendezvous rendezvous;
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		Assert.isTrue(this.actorService.findActorByPrincipal().getUserAccount().getAuthorities().contains(auth));
		final User user = (User) this.actorService.findActorByPrincipal();
		//Check that rendezvous date is higher than actual
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(rendezvous.getMoment().after(new Date())); //Check rendezvous is not already finished.
		Assert.isTrue(user.getCreatedRendezvouses().contains(rendezvous)); //Check user has permission for edit that announcement

		if (announcement.getVersion() == 0)
			//The announcement moment is actual when the announcement is created 
			announcement.setMoment(new Date(System.currentTimeMillis() - 1000));
		//Rendezvous rend = rendezvousService.getRendezvousByAnnouncement(announcement.getId());

		result = this.announcementRepository.save(announcement);

		if (announcement.getVersion() == 0) {
			rendezvous = this.rendezvousService.findOne(rendezvousId);
			rendezvous.getAnnouncements().add(result);
			this.rendezvousService.save(rendezvous);
		}

		return result;

	}

	/**
	 * Delete the announcement passed as parameter
	 * 
	 * @param announcement
	 * @author Alejandro
	 */
	public void delete(final Announcement announcement) {

		assert announcement != null;
		assert announcement.getId() != 0;

		Assert.isTrue(this.announcementRepository.exists(announcement.getId()));

		Rendezvous rendezvous;
		User user;
		Authority auth;

		auth = new Authority();
		auth.setAuthority(Authority.ADMIN);

		//Checkear que el usuario es el creador o administrador
		rendezvous = this.rendezvousService.getRendezvousByAnnouncement(announcement.getId());
		user = this.userService.getCreatorUser(rendezvous.getId());
		Assert.isTrue(this.actorService.findActorByPrincipal().getUserAccount().getAuthorities().contains(auth) || user.equals(this.actorService.findActorByPrincipal()));

		rendezvous.getAnnouncements().remove(announcement);
		this.rendezvousService.save(rendezvous);

		this.announcementRepository.delete(announcement);

	}

	/**
	 * Reconstruct the Announcement passed as parameter
	 * 
	 * @param announcement
	 * @param binding
	 * 
	 * @return The reconstructed Announcement
	 * @author MJ
	 */
	public Announcement reconstruct(final Announcement announcement, final BindingResult binding) {
		Announcement result;

		if (announcement.getId() == 0) {
			result = announcement;
			result.setMoment(new Date(System.currentTimeMillis() - 1000));
		} else {
			result = this.announcementRepository.findOne(announcement.getId());
			result.setDescription(announcement.getDescription());
			result.setTitle(announcement.getTitle());
			result.setMoment(announcement.getMoment());
		}

		this.validator.validate(result, binding);
		return result;
	}
	public Collection<Announcement> findCreatedAnnouncementByUserOrderByMoment(final int userId) {
		Assert.isTrue(userId != 0);

		Collection<Announcement> result;

		result = this.announcementRepository.findCreatedAnnouncementByUserOrderByMoment(userId);

		return result;
	}
	/**
	 * This method flushes the repository, this forces the cache to be saved to the database, which then forces the test data to be validated. This is only used
	 * in tests
	 * 
	 * @author Alejandro
	 */
	public void flush() {
		this.announcementRepository.flush();
	}
}
