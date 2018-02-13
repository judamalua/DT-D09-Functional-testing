
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.UserRepository;
import domain.User;

@Service
@Transactional
public class UserService {

	// Managed repository --------------------------------------------------

	@Autowired
	private UserRepository	userRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public User create() {
		User result;

		result = new User();

		return result;
	}

	public Collection<User> findAll() {

		Collection<User> result;

		Assert.notNull(this.userRepository);
		result = this.userRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public User findOne(final int userId) {

		User result;

		result = this.userRepository.findOne(userId);

		return result;

	}

	public User save(final User user) {

		assert user != null;

		User result;

		result = this.userRepository.save(user);

		return result;

	}

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
		Collection<User> rcpvusers;

		rcpvusers = this.userRepository.findUsersRSVPs(rendezvousId);

		return rcpvusers;

	}

	/**
	 * That method returns the user that has created
	 * 
	 * @param rendezvousId
	 * @return a User
	 * @author Luis
	 */
	public User getCreatorUser(final int rendezvousId) {
		User user;

		user = this.userRepository.findCreatoUser(rendezvousId);

		return user;

	}

	/**
	 * That method returns the user author of the answer
	 * 
	 * @param answerId
	 * @return a User
	 * @author Luis
	 */
	public User getUserFromAnswerId(final int answerId) {
		User user;

		user = this.userRepository.getUserFromAnswerId(answerId);

		return user;

	}

}
