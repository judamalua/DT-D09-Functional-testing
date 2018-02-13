
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Rendezvous save(final Rendezvous rendezvous) {
		assert rendezvous != null;
		Rendezvous result;
		User user;

		user = this.actorService.findActorByPrincipal();

		Assert.isTrue(user instanceof User);

		if (rendezvous.getVersion() != 0)
			if (rendezvous.getFinalMode())
				Assert.isTrue(!this.rendezvousRepository.findOne(rendezvous.getId()).getFinalMode());

		result = this.rendezvousRepository.save(rendezvous);

		if (this.findAll().contains(rendezvous))

			return result;

	}

	public void delete(final Rendezvous rendezvous) {

		assert rendezvous != null;
		assert rendezvous.getId() != 0;

		Assert.isTrue(this.rendezvousRepository.exists(rendezvous.getId()));

		this.rendezvousRepository.delete(rendezvous);

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
}
