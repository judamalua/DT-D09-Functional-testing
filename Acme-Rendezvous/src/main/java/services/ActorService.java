
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.User;

@Service
@Transactional
public class ActorService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ActorRepository	actorRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------
	/**
	 * Get all the actors in the system
	 * 
	 * @return all the actors registered in the system
	 * @author MJ
	 */
	public Collection<Actor> findAll() {

		Collection<Actor> result;

		Assert.notNull(this.actorRepository);
		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * Get the actor with the id passed as parameter
	 * 
	 * @param actorId
	 * @return an actor with id equals to actorId
	 * 
	 * @author MJ
	 */
	public Actor findOne(final int actorId) {

		Actor result;

		result = this.actorRepository.findOne(actorId);

		return result;

	}

	/**
	 * Saves the actor passed as parameter
	 * 
	 * @param actor
	 * @return The actor saved in the system
	 */
	public Actor save(final Actor actor) {

		assert actor != null;

		Actor result;

		result = this.actorRepository.save(actor);

		return result;

	}

	/**
	 * Delete the actor passed as parameter
	 * 
	 * @param actor
	 */
	public void delete(final Actor actor) {

		assert actor != null;
		assert actor.getId() != 0;

		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		this.actorRepository.delete(actor);

	}

	/**
	 * Get the actor logged in the system
	 * 
	 * @return the actor logged in the system
	 */
	public Actor findActorByPrincipal() {
		UserAccount userAccount;
		Actor result;

		userAccount = LoginService.getPrincipal();
		result = this.findActorByUserAccount(userAccount);

		return result;
	}

	/**
	 * Get an actor with the UserAccount passed
	 * 
	 * @param userAccount
	 * @return The actor with the UserAccount
	 * @author MJ
	 */
	public Actor findActorByUserAccount(final UserAccount userAccount) {

		Assert.notNull(userAccount);

		Actor result;

		result = this.actorRepository.findActorByUserAccountId(userAccount.getId());

		return result;
	}

	/**
	 * Checks there is an actor logged in the system
	 * 
	 * @author MJ
	 */
	public void checkUserLogin() {
		Actor actor;

		actor = this.findActorByPrincipal();

		Assert.notNull(actor);
	}

	/**
	 * That method create and save in the system and actor
	 * 
	 * @param actor
	 * @return Actor
	 */
	public Actor registerActor(final Actor actor) {
		Actor result;
		String password;
		Md5PasswordEncoder encoder;

		Assert.notNull(actor.getUserAccount());
		Assert.isTrue(!this.actorRepository.exists((actor.getId())));

		encoder = new Md5PasswordEncoder();
		password = actor.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);

		actor.getUserAccount().setPassword(password);

		result = this.actorRepository.save(actor);

		return result;
	}

	/**
	 * That method create and save in the system and user
	 * 
	 * @param user
	 * @return a User
	 */
	public User registerUser(final User user) {
		String password;
		Md5PasswordEncoder encoder;
		User result;

		Assert.isTrue(!this.actorRepository.exists((user.getId())));

		encoder = new Md5PasswordEncoder();
		password = user.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);

		user.getUserAccount().setPassword(password);

		result = this.actorRepository.save(user);

		return result;
	}

	/**
	 * That method create and save in the system and user
	 * 
	 * @param user
	 * @return a User
	 */
	public Administrator registerAdmin(final Administrator admin) {
		String password;
		Md5PasswordEncoder encoder;
		Administrator result;

		Assert.isTrue(!this.actorRepository.exists((admin.getId())));

		encoder = new Md5PasswordEncoder();
		password = admin.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);

		admin.getUserAccount().setPassword(password);

		result = this.actorRepository.save(admin);

		return result;
	}
}
