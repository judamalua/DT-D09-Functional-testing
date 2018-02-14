
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.UserRepository;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class UserService {

	// Managed repository --------------------------------------------------

	@Autowired
	private UserRepository	userRepository;


	// Supporting services --------------------------------------------------

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
		final Collection<Rendezvous> RSVPRendezvouses;

		result = new User();
		createdRendezvouses = new HashSet<Rendezvous>();
		RSVPRendezvouses = new HashSet<Rendezvous>();
		comments = new HashSet<Comment>();

		result.setRSVPRendezvouses(RSVPRendezvouses);
		result.setCreatedRendezvouses(createdRendezvouses);
		result.setComments(comments);
		result.setRSVPRendezvouses(RSVPRendezvouses);

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

	/**
	 * That method returns a collections of users that has RCVPd a rendezvous
	 * 
	 * @param rendezvousId
	 * @return a collection of Users
	 * @author Luis
	 */
	public Collection<User> getRCPVusers(final int rendezvousId) {
		Collection<User> result;

		result = this.userRepository.findUsersRSVPs(rendezvousId);

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

}
