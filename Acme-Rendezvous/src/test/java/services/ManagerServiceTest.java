
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
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	@Autowired
	public ActorService		actorService;
	@Autowired
	public ManagerService	ManagerService;


	//******************************************Positive Methods*******************************************************************
	/**
	 * This test checks that a not registered manager can register himself in the system,without errors
	 * 
	 * @author Luis
	 * @throws java.text.ParseException
	 */
	@Test
	public void testRegisterANewManager() throws java.text.ParseException {
		Manager newManager;

		newManager = this.createStandardManager();
		this.UpdateDataBase(newManager);

	}

	/**
	 * This test checks that a not registered manager can register himself in the system,without errors
	 * and without optional attributes(address,phone number)
	 * 
	 * @author Luis
	 * @throws java.text.ParseException
	 */
	@Test
	public void testRegisterANewManagerWithoutOptionalAtributes() throws java.text.ParseException {
		Manager manager;

		manager = this.createStandardManager();
		manager.setPhoneNumber(null);
		manager.setPostalAddress(null);
		this.UpdateDataBase(manager);

	}

	/**
	 * This test checks that a manager can edit his profile correctly
	 * 
	 * @author Luis
	 */
	@Test
	public void testEditProfile() {
		super.authenticate("Manager1");

		Manager manager;

		manager = (Manager) this.actorService.findActorByPrincipal();
		manager.setPhoneNumber("688574478");
		manager.setPostalAddress("Calle Mar");
		this.UpdateDataBase(manager);

		super.unauthenticate();

	}

	//******************************************Negative Methods*******************************************************************
	/**
	 * This test checks that a not registered manager can´t register himself in the system,without a valid name
	 * 
	 * @author Luis
	 * @throws java.text.ParseException
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewManagerWithInvalidName() throws java.text.ParseException {
		Manager manager;

		manager = this.createStandardManager();
		manager.setName("");//Invalid:is Blank
		this.UpdateDataBase(manager);

	}

	/**
	 * This test checks that a not registered manager can´t register himself in the system,without a valid surname
	 * 
	 * @author Luis
	 * @throws java.text.ParseException
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewManagerWithInvalidSurname() throws java.text.ParseException {
		Manager manager;

		manager = this.createStandardManager();
		manager.setSurname("");//Invalid:is Blank
		this.UpdateDataBase(manager);

	}
	/**
	 * This test checks that a not registered manager can´t register himself in the system,without a valid email
	 * 
	 * @author Luis
	 * @throws java.text.ParseException
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewManagerWithInvalidEmail() throws java.text.ParseException {
		Manager manager;

		manager = this.createStandardManager();
		manager.setEmail("invalidEmail");//Invalid:not email format
		this.UpdateDataBase(manager);

	}

	/**
	 * This test checks that a not registered manager can´t register himself in the system,without a valid birth date
	 * 
	 * @author Luis
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewManagerWithInvalidBirthDate() throws ParseException, java.text.ParseException {
		Manager manager;
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		final Date birthDate = format.parse("21/12/2030");

		manager = this.createStandardManager();
		manager.setBirthDate(birthDate);//Invalid:future date
		this.UpdateDataBase(manager);

	}

	/**
	 * This test checks that a manager can´t edit the profile of other manager
	 * 
	 * @author Luis
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEditProfileOfOtherManager() {
		super.authenticate("Manager1");

		Manager manager;
		Integer managerId;

		managerId = super.getEntityId("Manager2");
		manager = this.ManagerService.findOne(managerId);

		manager.setPhoneNumber("658877784");
		manager.setEmail("manager2newEmail@gmail.com");
		manager.setPostalAddress("Calle Alfarería 15b");

		this.UpdateDataBase(manager);

		super.unauthenticate();

	}

	/**
	 * This test checks that a no logged actor can´t edit the profile of a manager
	 * 
	 * @author Luis
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoLoggedManagerEditProfileOfOtherManager() {
		super.authenticate(null);

		Manager manager;
		Integer managerId;

		managerId = super.getEntityId("Manager2");
		manager = this.ManagerService.findOne(managerId);

		manager.setPhoneNumber("658877784");
		manager.setEmail("manager2newEmail@gmail.com");
		manager.setPostalAddress("Calle Alfarería 15b");

		this.UpdateDataBase(manager);

		super.unauthenticate();

	}

	/**
	 * This method create a manager with correct attributes
	 * 
	 * @author Luis
	 */
	private Manager createStandardManager() throws java.text.ParseException {
		Manager manager;
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		final Date birthDate = format.parse("21/12/2000");

		manager = this.ManagerService.create();
		manager.setName("Antonio");
		manager.setSurname("Gómez Pérez");
		manager.setBirthDate(birthDate);
		manager.setEmail("antoniogope@gmail.com");
		manager.setPhoneNumber("6011117789");
		manager.setPostalAddress("Calle Manuel Machado 9");
		manager.setVat("ESQW23-1-2");

		return manager;

	}

	/**
	 * This method try to update a Entity in database(in this case Manager)
	 * 
	 * @author Luis
	 */
	private void UpdateDataBase(final Manager manager) {
		this.ManagerService.save(manager);
		this.ManagerService.flush();
	}

}
