
package services;

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

import repositories.UserRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Comment;
import domain.Rendezvous;
import domain.User;
import forms.UserAdminForm;

@Service
@Transactional
public class UserService {

	// Managed repository --------------------------------------------------

	@Autowired
	private UserRepository	userRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private Validator		validator;
	@Autowired
	private ActorService	actorService;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * That method create a instance of a user
	 * 
	 * @return User
	 * @author Luis
	 */
	public User create() {
		User result;
		final Collection<Comment> comments;
		final Collection<Rendezvous> createdRendezvouses;
		final Collection<Rendezvous> rsvpsRendezvouses;
		final UserAccount userAccount;
		final Collection<Authority> auth;
		final Authority authority;

		result = new User();

		userAccount = new UserAccount();
		auth = new HashSet<Authority>();
		authority = new Authority();
		authority.setAuthority(Authority.USER);
		auth.add(authority);
		userAccount.setAuthorities(auth);

		result.setUserAccount(userAccount);

		createdRendezvouses = new HashSet<Rendezvous>();
		rsvpsRendezvouses = new HashSet<Rendezvous>();
		comments = new HashSet<Comment>();

		result.setCreatedRendezvouses(createdRendezvouses);
		result.setComments(comments);
		result.setRsvpRendezvouses(rsvpsRendezvouses);

		return result;
	}

	/**
	 * That method returns a collection with all of users of the system
	 * 
	 * @return Collection<User>
	 * @author Luis
	 */
	public Collection<User> findAll() {
		Collection<User> result;

		Assert.notNull(this.userRepository);

		result = this.userRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * 
	 * That method return an user from his id
	 * 
	 * @param userId
	 * @return User
	 * @author Luis
	 */
	public User findOne(final int userId) {

		User result;

		result = this.userRepository.findOne(userId);

		return result;

	}

	/**
	 * 
	 * That method save a user in the system
	 * 
	 * @param user
	 * @return the saved User
	 * @author Luis
	 */
	public User save(final User user) {
		assert user != null;
		Actor actor = null;

		if (user.getId() != 0)
			actor = this.actorService.findActorByPrincipal();

		if (actor instanceof User && user.getId() != 0)
			Assert.isTrue(user.equals(actor));

		User result;

		result = this.userRepository.save(user);

		return result;

	}

	/**
	 * That method remove a user from the system
	 * 
	 * @param user
	 * @author Luis
	 */
	public void delete(final User user) {

		assert user != null;
		assert user.getId() != 0;

		Assert.isTrue(this.userRepository.exists(user.getId()));

		this.userRepository.delete(user);

	}

	// Other business methods ----------------------------------------------------------------

	public User reconstruct(final UserAdminForm userAdminForm, final BindingResult binding) {
		User result;

		if (userAdminForm.getId() == 0) {

			Collection<Comment> comments;
			Collection<Rendezvous> createdRendezvouses, RSVPedRendezvouses;

			createdRendezvouses = new HashSet<Rendezvous>();
			RSVPedRendezvouses = new HashSet<Rendezvous>();
			comments = new HashSet<Comment>();

			result = this.create();

			result.getUserAccount().setUsername(userAdminForm.getUserAccount().getUsername());
			result.getUserAccount().setPassword(userAdminForm.getUserAccount().getPassword());
			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());

			result.setCreatedRendezvouses(createdRendezvouses);
			result.setComments(comments);
			result.setRsvpRendezvouses(RSVPedRendezvouses);

		} else {
			result = this.userRepository.findOne(userAdminForm.getId());

			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());

		}

		this.validator.validate(result, binding);

		return result;
	}

	/**
	 * That method returns a collections of users of the system with pageable
	 * 
	 * @param pageable
	 * @return Page<Users>
	 * @author Luis
	 */
	public Page<User> getUsers(final Pageable pageable) {
		Page<User> result;

		result = this.userRepository.findAll(pageable);

		return result;

	}

	/**
	 * That method returns the user that has created
	 * 
	 * @param rendezvousId
	 * @return a User
	 * @author Luis
	 */
	public User getCreatorUser(final int rendezvousId) {
		User result;

		result = this.userRepository.findCreatoUser(rendezvousId);

		return result;

	}

	// Dashboard queries

	/**
	 * Level C query 1
	 * 
	 * @return The average and the standard deviation of rendezvouses created per user.
	 * @author Juanmi
	 */
	public String getRendezvousesInfoFromUsers() {
		String result;

		result = this.userRepository.getRendezvousesInfoFromUsers();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level C query 2
	 * 
	 * @return The ratio of users who have ever created a rendezvous versus the users who have never created any rendezvouses.
	 * @author Juanmi
	 */
	public String getRatioCreatedRendezvouses() {
		String result;

		result = this.userRepository.getRatioCreatedRendezvouses();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level C query 4
	 * 
	 * @return The average and the standard deviation of rendezvouses that are RSVPd per user.
	 * @author Juanmi
	 */
	public String getRSVPedInfoFromRendezvous() {
		String result;

		result = this.userRepository.getRSVPedInfoFromRendezvous();

		Assert.notNull(result);

		return result;
	}
	/**
	 * 
	 * 
	 * 
	 * @author Luis
	 */
	public void flush() {
		this.userRepository.flush();
	}

}
