
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RendezvousRepository;
import domain.Actor;
import domain.Administrator;
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
	@Autowired
	private UserService				userService;
	@Autowired
	private AnnouncementService		announcementService;
	@Autowired
	private QuestionService			questionService;
	@Autowired
	private CommentService			commentService;

	@Autowired
	private Validator				validator;


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
		User user;
		final Collection<Question> questions;
		final Collection<Rendezvous> similars;
		final Collection<Announcement> announcements;
		final Collection<Comment> comments;
		final Collection<User> users;

		questions = new HashSet<Question>();
		similars = new HashSet<Rendezvous>();
		announcements = new HashSet<Announcement>();
		comments = new HashSet<Comment>();
		users = new HashSet<User>();

		user = (User) this.actorService.findActorByPrincipal();
		users.add(user);

		result = new Rendezvous();

		result.setQuestions(questions);
		result.setAnnouncements(announcements);
		result.setComments(comments);
		result.setSimilars(similars);
		result.setUsers(users);

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
	 * provides to the method, but it doesn't check if it has been marked as deleted.
	 * 
	 * @param rendezvousId
	 * @return This method return Rendezvous
	 * @author Antonio
	 */
	public Rendezvous findOneForReplies(final int rendezvousId) {
		Rendezvous result;

		result = this.rendezvousRepository.findOne(rendezvousId);

		return result;
	}

	/**
	 * 
	 * This method save a Rendezvous created or edited by a user
	 * 
	 * @param rendezvous
	 * @return This method return Rendezvous
	 * @author Luis
	 */
	public Rendezvous save(final Rendezvous rendezvous) {
		assert rendezvous != null;
		Rendezvous result;
		User user;
		Actor actor;

		actor = this.actorService.findActorByPrincipal();

		if (rendezvous.getAdultOnly())
			Assert.isTrue(this.actorService.checkUserIsAdult(actor));

		if (actor instanceof User) {
			user = (User) this.actorService.findActorByPrincipal();

			rendezvous.getSimilars().remove(null);//Nedded to not have errors

			result = this.rendezvousRepository.save(rendezvous);

			if (user.getCreatedRendezvouses().contains(rendezvous))   		//
				user.getCreatedRendezvouses().remove(rendezvous);			//UPDATING USER
			user.getCreatedRendezvouses().add(result);						//

			this.actorService.save(user);
		} else
			result = this.rendezvousRepository.save(rendezvous);

		return result;
	}

	/**
	 * 
	 * This method save a Rendezvous commented by an user
	 * 
	 * @param rendezvous
	 * @return This method return Rendezvous
	 * @author Antonio
	 */
	public Rendezvous comment(final Rendezvous rendezvous) {
		Assert.notNull(rendezvous);
		Assert.isTrue(rendezvous.getComments().size() != 0);

		Rendezvous result;

		result = this.rendezvousRepository.save(rendezvous);

		return result;
	}

	/**
	 * 
	 * This method save a Rendezvous in RSVPRendevouses(Collection<Rendevous>), that means that the user
	 * has been joined to this Rendezvous
	 * 
	 * @param rendesvous
	 * @return This method return Rendezvous
	 * @author Luis
	 */
	public Rendezvous RSVP(final Rendezvous rendezvous) {
		assert rendezvous != null;
		Rendezvous result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();

		if (rendezvous.getUsers().contains(user))   				//
			rendezvous.getUsers().remove(user);						//UPDATING USER
		rendezvous.getUsers().add(user);							//

		result = this.rendezvousRepository.save(rendezvous);

		this.actorService.save(user);

		return result;
	}

	/**
	 * Let's the principal user leave a Rendezvous
	 * 
	 * @param rendezvous
	 *            The rendezvous to leave
	 * @author Daniel Diment
	 * @return
	 *         The updated rendezvous
	 */
	public Rendezvous disRSVP(final Rendezvous rendezvous) {
		assert rendezvous != null;
		Rendezvous result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();

		rendezvous.getUsers().remove(user);						//Remove user

		result = this.rendezvousRepository.save(rendezvous);

		this.actorService.save(user);

		return result;
	}

	/**
	 * 
	 * This method set rendezvous boolean "deleted" to true, that means that it is not deleted in DB,
	 * but we mark it like deleted.
	 * 
	 * @param rendezvous
	 * 
	 * @author Luis
	 */
	public void delete(final Rendezvous rendezvous) {
		this.actorService.checkUserLogin();

		assert rendezvous != null;
		assert rendezvous.getId() != 0;

		Actor actor;
		User user;

		Assert.isTrue(this.rendezvousRepository.exists(rendezvous.getId()));
		actor = this.actorService.findActorByPrincipal();
		user = this.userService.getCreatorUser(rendezvous.getId());

		if (actor instanceof Administrator) {

			// Deleting Announcements of the Rendezvous that is about to be deleted
			for (final Announcement announcement : rendezvous.getAnnouncements())
				this.announcementService.delete(announcement);

			// Deleting Questions of the Rendezvous that is about to be deleted
			for (final Question question : rendezvous.getQuestions())
				this.questionService.delete(question);

			// Deleting Comments of the Rendezvous that is about to be deleted
			for (final Comment comment : new ArrayList<Comment>(rendezvous.getComments()))
				this.commentService.deleteCommentFromRendezvous(comment);

			user.getCreatedRendezvouses().remove(rendezvous); // Deleting rendezvous from user list when an admin deletes a Rendezvous
			this.actorService.save(user);

			this.rendezvousRepository.delete(rendezvous);

		} else {
			rendezvous.setDeleted(true);
			this.save(rendezvous);
		}

	}

	// Other business methods --------------------------------------------------

	public Rendezvous reconstruct(final Rendezvous rendezvous, final BindingResult binding) {
		Rendezvous result;

		if (rendezvous.getId() == 0) {
			User user;
			Collection<Question> questions;
			Collection<Rendezvous> similars;
			Collection<Announcement> announcements;
			Collection<Comment> comments;
			Collection<User> users;

			questions = new HashSet<Question>();
			similars = rendezvous.getSimilars();
			announcements = new HashSet<Announcement>();
			comments = new HashSet<Comment>();
			users = new HashSet<User>();
			user = (User) this.actorService.findActorByPrincipal();
			users.add(user);
			result = rendezvous;

			result.setQuestions(questions);
			result.setAnnouncements(announcements);
			result.setComments(comments);
			result.setSimilars(similars);
			result.setUsers(users);
		} else {
			result = this.rendezvousRepository.findOne(rendezvous.getId());

			// Checking that the rendezvous that is trying to be saved
			Assert.isTrue(!result.getFinalMode());

			result.setName(rendezvous.getName());
			result.setDescription(rendezvous.getDescription());
			result.setMoment(rendezvous.getMoment());
			result.setPictureUrl(rendezvous.getPictureUrl());
			result.setGpsCoordinates(rendezvous.getGpsCoordinates());
			result.setFinalMode(rendezvous.getFinalMode());
			result.setDeleted(rendezvous.getDeleted());
			result.setAdultOnly(rendezvous.getAdultOnly());

			this.validator.validate(result, binding);
		}

		return result;
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
	public Page<Rendezvous> findRendezvousbySimilar(final int id, final Pageable pageable) {
		Assert.isTrue(id != 0);
		Assert.notNull(pageable);

		Page<Rendezvous> result;

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
	 * Return the list of rendezvouses in final mode and not deleted
	 * 
	 * @return A page of Rendezvouses in final mode
	 * @author MJ
	 */
	public Collection<Rendezvous> findFinalRendezvouses() {

		Collection<Rendezvous> result;

		result = this.rendezvousRepository.findFinalRendezvouses();

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

	// Dashboard queries.

	/**
	 * Level C query 3
	 * 
	 * @return The average and the standard deviation of users per rendezvous.
	 * @author Juanmi
	 */
	public String getUsersInfoFromRendezvous() {
		String result;

		result = this.rendezvousRepository.getUsersInfoFromRendezvous();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level C query 4 part 1/2
	 * 
	 * @return The average of rendezvouses that are RSVPd per user as the first element of the array and RSVPed users as the second element.
	 * @author Juanmi
	 */
	public String[] getAverageRSVPedPerUser() {
		final String[] result = {
			"", ""
		};
		Float average;

		Collection<Rendezvous> allRendezvouses;
		Collection<User> allUsers;

		Float RSVPedUsers = 0F;
		allRendezvouses = this.findAll();
		allUsers = this.userService.findAll();

		for (final Rendezvous rendezvous : allRendezvouses)
			RSVPedUsers += new Float(rendezvous.getUsers().size() - 1);

		average = RSVPedUsers / new Float(allUsers.size());

		result[0] = average.toString();
		result[1] = RSVPedUsers.toString();

		return result;
	}
	//sqrt(sum(r.users.size * r.users.size) / count(r.users.size) - (avg(r.users.size) * avg(r.users.size)))
	/**
	 * Level C query 4 part 2/2
	 * 
	 * @return The standard deviation of rendezvouses that are RSVPd per user.
	 */
	public String getStandardDeviationRSVPedPerUser() {
		String[] averageRSVPedUsers;
		Float average, totalUsers, standardDeviation, RSVPedUsers;
		String result;

		averageRSVPedUsers = this.getAverageRSVPedPerUser();

		average = new Float(averageRSVPedUsers[0]);
		RSVPedUsers = new Float(averageRSVPedUsers[1]);

		totalUsers = new Float(this.userService.findAll().size());

		standardDeviation = (float) ((Math.sqrt(RSVPedUsers * RSVPedUsers) / totalUsers) - (average * average));

		result = standardDeviation.toString();

		return result;
	}

	/**
	 * Level C query 5
	 * 
	 * @return The top-10 rendezvouses in terms of users who have RSVPd them.
	 * @author Juanmi
	 */
	public Collection<Rendezvous> getTopTenRendezvouses() {
		Collection<Rendezvous> allRendezvouses;
		final Collection<Rendezvous> result = new HashSet<Rendezvous>();

		allRendezvouses = this.rendezvousRepository.getTopRendezvouses();

		if (allRendezvouses.size() > 10) {
			for (final Rendezvous rendezvous : allRendezvouses)
				if (result.size() < 10)
					result.add(rendezvous);

		} else
			result.addAll(allRendezvouses);

		return result;
	}

	/**
	 * Level B query 1
	 * 
	 * @return The average and the standard deviation of announcements per rendezvous.
	 * @author Juanmi
	 */
	public String getAnnouncementsInfoFromRendezvous() {
		String result;

		result = this.rendezvousRepository.getAnnouncementsInfoFromRendezvous();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level B query 2
	 * 
	 * @return The rendezvouses whose number of announcements is above 75% the average number of announcements per rendezvous.
	 * @author Juanmi
	 */
	public Collection<Rendezvous> getRendezvousesWithAnnouncementAboveSeventyFivePercent() {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.getRendezvousesWithAnnouncementAboveSeventyFivePercent();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level B query 3
	 * 
	 * @return The rendezvouses that are linked to a number of rendezvouses that is greater than the average plus 10%.
	 * @author Juanmi
	 */
	public Collection<Rendezvous> getRendezvousesMostLinked() {
		Collection<Rendezvous> result;

		result = this.rendezvousRepository.getRendezvousesMostLinked();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level A query 1
	 * 
	 * @return The average and the standard deviation of the number of questions per rendezvous.
	 * @author Juanmi
	 */
	public String getQuestionsInfoFromRendezvous() {
		String result;

		result = this.rendezvousRepository.getQuestionsInfoFromRendezvous();

		Assert.notNull(result);

		return result;
	}
}
