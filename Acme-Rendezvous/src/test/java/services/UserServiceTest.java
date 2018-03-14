
package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	@Autowired
	public ActorService	actorService;
	@Autowired
	public UserService	userService;


	//******************************************Positive Methods*******************************************************************
	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * 
	 * This test checks that a not registered user can register himself in the system,without errors
	 * 
	 * @author Luis
	 */
	@Test
	public void testRegisterANewUser() {
		User newUser;
		final Date birthDate = new Date();

		newUser = this.userService.create();

		newUser.setName("Fernando");
		newUser.setSurname("Gutiérrez López");
		newUser.setBirthDate(birthDate);
		newUser.setEmail("ferguti90@gmail.com");
		newUser.setPhoneNumber("606587789");
		newUser.setPostalAddress("Calle Picadero 9");

		this.userService.save(newUser);

	}

	/**
	 * 4.2 An actor who is not authenticated must be able to:List the users of the system and navigate to their profiles, which include personal data and the list of rendezvouses that they've attended or are going to attend.
	 * 
	 * 
	 * This test checks that a not registered actor can list the users in the system and navigate to their profiles
	 * 
	 * @author Luis
	 */
	@Test
	public void testActorNotRegisterdedCanListUsers() {
		super.authenticate(null);

		this.userService.findAll();//List Users in the system

		this.userService.findOne(this.getEntityId("User2"));//Navigate to their profiles

		super.unauthenticate();

	}

	/**
	 * 5.1 An actor who is authenticated as a user must be able to:Do the same as an actor who is not authenticated, but register to the system.
	 * 
	 * This test checks that a registered actor can list the users in the system and navigate to their profiles
	 * 
	 * @author Luis
	 */
	@Test
	public void testActorRegisterdedCanListUsers() {
		super.authenticate("User1");

		this.userService.findAll();//List Users in the system

		this.userService.findOne(this.getEntityId("User2"));//Navigate to their profiles

		super.unauthenticate();

	}

	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * 
	 * This test checks that a not registered user can register himself in the system,without errors
	 * and without optional attributes(address,phone number)
	 * 
	 * @author Luis
	 */
	@Test
	public void testRegisterANewUserWithoutOptionalAtributes() {
		User newUser;
		final Date birthDate = new Date();

		newUser = this.userService.create();

		newUser.setName("Fernando");
		newUser.setSurname("Gutiérrez López");
		newUser.setBirthDate(birthDate);
		newUser.setEmail("ferguti90@gmail.com");

		this.userService.save(newUser);

		super.unauthenticate();
	}

	/**
	 * 
	 * 
	 * This test checks that a user can edit his profile correctly
	 * 
	 * @author Luis
	 */
	@Test
	public void testEditProfile() {
		super.authenticate("User1");

		User user;

		user = (User) this.actorService.findActorByPrincipal();
		user.setPhoneNumber("658877877");
		user.setEmail("user1newEmail@gmail.com");
		user.setPostalAddress("Calle Capitanía 13a");

		this.userService.save(user);

	}

	//******************************************Negative Methods*******************************************************************
	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * 
	 * This test checks that a not registered user cannot register himself in the system,without a valid name
	 * 
	 * @author Luis
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewUserWithInvalidName() {
		User newUser;

		final Date birthDate = new Date();

		newUser = this.userService.create();

		newUser.setName("");//Name not valid(is blank)
		newUser.setSurname("Gutiérrez López");
		newUser.setBirthDate(birthDate);
		newUser.setEmail("ferguti90@gmail.com");
		newUser.setPhoneNumber("606587789");
		newUser.setPostalAddress("Calle Picadero 9");

		this.userService.save(newUser);
		this.userService.flush();

	}

	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * This test checks that a not registered user cannot register himself in the system,without a valid surname
	 * 
	 * @author Luis
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewUserWithInvalidSurname() {
		User newUser;

		final Date birthDate = new Date();

		newUser = this.userService.create();

		newUser.setName("Fernando");
		newUser.setSurname("");//Surname not valid(is blank)
		newUser.setBirthDate(birthDate);
		newUser.setEmail("ferguti90@gmail.com");
		newUser.setPhoneNumber("606587789");
		newUser.setPostalAddress("Calle Picadero 9");

		this.userService.save(newUser);
		this.userService.flush();

	}
	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * This test checks that a not registered user cannot register himself in the system,without a valid email
	 * 
	 * @author Luis
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewUserWithInvalidEmail() {
		User newUser;

		final Date birthDate = new Date();

		newUser = this.userService.create();

		newUser.setName("Fernando");
		newUser.setSurname("Gutiérrez López");
		newUser.setBirthDate(birthDate);
		newUser.setEmail("ferguti90");//Email not valid(don´t follow the pattern of a email )
		newUser.setPhoneNumber("606587789");
		newUser.setPostalAddress("Calle Picadero 9");

		this.userService.save(newUser);
		this.userService.flush();

	}

	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * 
	 * This test checks that a not registered user cannot register himself in the system,without a valid birth date
	 * 
	 * @author Luis
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewUserWithInvalidBirthDate() throws ParseException, java.text.ParseException {
		User newUser;
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		final Date birthDate = format.parse("21/12/2030");

		newUser = this.userService.create();

		newUser.setName("Fernando");
		newUser.setSurname("Gutiérrez López");
		newUser.setBirthDate(birthDate);//Birth date not valid(future date)
		newUser.setEmail("ferguti90@gmail.com");
		newUser.setPhoneNumber("606587789");
		newUser.setPostalAddress("Calle Picadero 9");

		this.userService.save(newUser);
		this.userService.flush();

	}

	/**
	 * 
	 * 
	 * This test checks that a user cannot edit the profile of other user
	 * 
	 * @author Luis
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEditProfileOfOtherUser() {
		super.authenticate("User1");

		User user;
		Integer userId;

		userId = super.getEntityId("User2");
		user = this.userService.findOne(userId);

		user.setPhoneNumber("658877784");
		user.setEmail("user2newEmail@gmail.com");
		user.setPostalAddress("Calle Alfarería 15b");

		this.userService.save(user);

		super.unauthenticate();

	}

	/**
	 * 
	 * 
	 * This test checks that unauthenticated users cannot edit the profile of other user
	 * 
	 * @author Luis
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoLoggedUserEditProfileOfOtherUser() {
		super.authenticate(null);

		User user;
		Integer userId;

		userId = super.getEntityId("User2");
		user = this.userService.findOne(userId);

		user.setPhoneNumber("658877784");
		user.setEmail("user2newEmail@gmail.com");
		user.setPostalAddress("Calle Alfarería 15b");

		this.userService.save(user);

		super.unauthenticate();

	}

}
