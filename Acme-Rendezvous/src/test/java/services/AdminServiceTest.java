
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private AdministratorService	adminService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverCreateAdmin() {

		//TODO: Preguntar sobre la modificacion de announcement create/delete
		final Object testingData[][] = {
			{
				"Admin1", "TestName", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", null
			}, {
				"Admin1", "", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", IllegalArgumentException.class
			}, {
				null, "TestName", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", IllegalArgumentException.class
			}, {
				"User1", "TestName", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", IllegalArgumentException.class
			},

		};
		for (int i = 0; i < testingData.length; i++) {
			System.out.println(i);
			this.templateCreateAdmin((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

		}
	}

	/**
	 * This test checks that authenticated users can answer to questions
	 * as said in functional requirement 21.2: An actor who is authenticated as
	 * a user must be able to answer the questions that are associated with a rendezvous
	 * that he or she’s RSVP-ing now.
	 * 
	 * @author Alejandro
	 */

	public void templateCreateAdmin(final String login, final String name, final String surname, final Date birthDate, final String email, final String phoneNumber, final String address, final Class<?> expected) {
		// Functional requirement number 21.2: An actor who is authenticated as a user must be able to Answer the questions that are associated
		// with a rendezvous that he or she’s RSVPing now.
		Administrator newAdmin;

		super.authenticate(login);

		newAdmin = this.adminService.create();
		//EN PROGRESO, Falta crear la useraccount

		newAdmin.setName(name);
		newAdmin.setSurname(surname);
		newAdmin.setBirthDate(birthDate);
		newAdmin.setEmail(email);
		newAdmin.setPhoneNumber(phoneNumber);
		newAdmin.setPostalAddress(address);

		this.adminService.save(newAdmin);
		this.adminService.flush();

		super.unauthenticate();
	}

}
