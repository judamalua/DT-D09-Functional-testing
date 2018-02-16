
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
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
	 * That method create a instance of a user
	 * 
	 * @return User
	 * @author Luis
	 */
	public Administrator createAdmin() {
		Administrator result;

		result = new Administrator();

		final UserAccount ua = new UserAccount();
		final Collection<Authority> auth = new HashSet<Authority>();
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		auth.add(a);
		ua.setAuthorities(auth);

		result.setUserAccount(ua);

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
	 */
	public void checkUserLogin() {
		Actor actor;

		actor = this.findActorByPrincipal();

		Assert.notNull(actor);
	}

	public int getAge(final User user) {
		Assert.notNull(user);

		final int result;
		LocalDate birthDay;
		LocalDate currentDate;

		currentDate = LocalDate.now();
		birthDay = LocalDate.fromDateFields(user.getBirthDate());
		result = Years.yearsBetween(birthDay, currentDate).getYears();
		Assert.isTrue(result > 0);

		return result;
	}

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

}
