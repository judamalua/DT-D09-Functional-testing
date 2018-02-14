
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RendezvousRepository;
import domain.Announcement;
import domain.Comment;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class RendezvousService {

	// Managed repository --------------------------------------------------

	@Autowired
	private RendezvousRepository	rendezvousRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * 
	 * This method create a new instance of Rendezvous, and initialise the collections of the relationship
	 * to a new empty one
	 * 
	 * @return This method return a new instance of Rendezvous
	 * @author Luis
	 */
	public Rendezvous create() {
		Rendezvous result;
		final Collection<Question> questions;
		final Collection<Rendezvous> similars;
		final Collection<Announcement> announcements;
		final Collection<Comment> comments;

		questions = new HashSet<Question>();
		similars = new HashSet<Rendezvous>();
		announcements = new HashSet<Announcement>();
		comments = new HashSet<Comment>();

		result = new Rendezvous();

		result.setQuestions(questions);
		result.setAnnouncements(announcements);
		result.setComments(comments);
		result.setSimilars(similars);

		return result;

	}

	/**
	 * 
	 * This method returns collections with all of Rendezvous that are stored in the system
	 * 
	 * @return This method return collection of Rendezvous
	 * @author Luis
	 */
	public Collection<Rendezvous> findAll() {

		Collection<Rendezvous> result;

		Assert.notNull(this.rendezvousRepository);
		result = this.rendezvousRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * 
	 * This method returns a Rendezvous witch id its equals to the id that you
	 * provides to the method
	 * 
	 * @param rendesvousId
	 * @return This method return Rendezvous
	 * @author Luis
	 */
	public Rendezvous findOne(final int rendezvousId) {

		Rendezvous result;

		result = this.rendezvousRepository.findOne(rendezvousId);

		Assert.isTrue(!result.getFinalMode());
		Assert.isTrue(!result.getDeleted());

		return result;

	}

	/**
	 * 
	 * This method save a Rendezvous created or edited by a user
	 * 
	 * @param rendesvous
	 * @return This method return Rendezvous
	 * @author Luis
	 */
	public Rendezvous save(final Rendezvous rendezvous) {
		assert rendezvous != null;
		Rendezvous result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();

		result = this.rendezvousRepository.save(rendezvous);

		if (user.getCreatedRendezvouses().contains(rendezvous))   		//
			user.getCreatedRendezvouses().remove(rendezvous);			//UPDATING USER
		user.getCreatedRendezvouses().add(rendezvous);					//

		this.actorService.save(user);

		return result;
	}

	/**
	 * 
	 * This method save a Rendezvous in RCVPRendevouses(Collection<Rendevous>), that means that the user
	 * has been joined to this Rendezvous
	 * 
	 * @param rendesvous
	 * @return This method return Rendezvous
	 * @author Luis
	 */
	public Rendezvous RCVP(final Rendezvous rendezvous) {
		assert rendezvous != null;
		Rendezvous result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();

		result = this.rendezvousRepository.save(rendezvous);

		if (user.getRSVPRendezvouses().contains(rendezvous))			//
			user.getRSVPRendezvouses().remove(rendezvous);				//UPDATING USER
		user.getRSVPRendezvouses().add(rendezvous);						//

		this.actorService.save(user);

		return result;
	}

	/**
	 * 
	 * This method set rendezvous boolean "deleted" to true, that means that it is not deleted in DB,
	 * but we mark it like deleted.
	 * 
	 * @param rendesvous
	 * 
	 * @author Luis
	 */
	public void delete(final Rendezvous rendezvous) {
		assert rendezvous != null;
		assert rendezvous.getId() != 0;
		Rendezvous r;

		Assert.isTrue(this.rendezvousRepository.exists(rendezvous.getId()));
		r = this.findOne(rendezvous.getId());
		r.setDeleted(true);

		this.save(r);

	}

	/**
	 * 
	 * This method returns the Rendezvous that has the question which id its provided
	 * 
	 * @param questionId
	 * @return This method return a Rendezvous
	 * @author Luis
	 */
	public Rendezvous getRendezvousByQuestion(final int questionId) {
		Rendezvous result;

		result = this.rendezvousRepository.findRendezvousbyQuestion(questionId);

		return result;

	}

	/**
	 * 
	 * This method returns the Rendezvous that has the commentary which id its provided
	 * 
	 * @param commentaryId
	 * @return This method return a Rendezvous
	 * @author Luis
	 */
	public Rendezvous getRendezvousByCommentary(final int commentaryId) {
		Rendezvous result;

		result = this.rendezvousRepository.findRendezvousbyCommentary(commentaryId);

		return result;

	}

	/**
	 * 
	 * This method returns the Rendezvous that has the announcement which id its provided
	 * 
	 * @param announcementId
	 * @return This method return a Rendezvous
	 * @author Luis
	 */
	public Rendezvous getRendezvousByAnnouncement(final int announcementId) {
		Rendezvous result;

		result = this.rendezvousRepository.findRendezvousbyAnnouncement(announcementId);

		return result;

	}

	/**
	 * Get the rendezvous associated to a similar rendezvous
	 * 
	 * @param id
	 * @param pageable
	 * @return The rendezvous associated
	 * @author MJ
	 */
	public Collection<Rendezvous> findRendezvousbySimilar(final int id, final Pageable pageable) {
		Assert.isTrue(id != 0);
		Assert.notNull(pageable);

		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findRendezvousbySimilar(id, pageable);

		return result;
	}

	/**
	 * Return the list of rendezvouses in final mode and not deleted paginated by the param
	 * 
	 * @param pageable
	 * @return A page of Rendezvouses in final mode
	 * @author MJ
	 */
	public Page<Rendezvous> findFinalRendezvouses(final Pageable pageable) {
		Assert.notNull(pageable);

		Page<Rendezvous> result;

		result = this.rendezvousRepository.findFinalRendezvouses(pageable);

		return result;
	}

	/**
	 * Return the list of rendezvouses in final mode, not deleted and
	 * without adult content paginated by the param
	 * 
	 * @param pageable
	 * @return A page of Rendezvouses in final mode without adult content
	 * @author MJ
	 */
	public Page<Rendezvous> findFinalWithoutAdultRendezvouses(final Pageable pageable) {
		Assert.notNull(pageable);

		Page<Rendezvous> result;

		result = this.rendezvousRepository.findFinalWithoutAdultRendezvouses(pageable);

		return result;
	}

	/**
	 * Return the list of not deleted rendezvouses paginated by the pageable and created by user
	 * 
	 * @param pageable
	 * @param user
	 * @return A page of Rendezvouses created by the user
	 * @author MJ
	 */
	public Page<Rendezvous> findCreatedRendezvouses(final User user, final Pageable pageable) {
		Assert.notNull(pageable);
		Assert.notNull(user);

		Page<Rendezvous> result;

		result = this.rendezvousRepository.findCreatedRendezvouses(user.getId(), pageable);

		return result;
	}

	/**
	 * Return the list of not deleted rendezvouses paginated by the pageable and RSVP by user
	 * 
	 * @param pageable
	 * @param user
	 * @return A page of Rendezvouses RSVP by the user
	 * @author MJ
	 */
	public Page<Rendezvous> findRSVPRendezvouses(final User user, final Pageable pageable) {
		Assert.notNull(pageable);
		Assert.notNull(user);

		Page<Rendezvous> result;

		result = this.rendezvousRepository.findRSVPRendezvouses(user.getId(), pageable);

		return result;
	}

}
