
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.DomainService;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private RequestService		requestService;
	@Autowired
	private ServiceService		serviceService;
	@Autowired
	private CreditCardService	creditCardService;


	// Tests ------------------------------------------------------------------
	/**
	 * This driver checks several tests regarding the following functional requirement:
	 * 
	 * 4.3. Request a service for one of the rendezvouses that he or she's created. He or she
	 * must specify a valid credit card in every request for a service. Optionally, he or she
	 * can provide some comments in the request.
	 * 
	 * @author Daniel Diment
	 */
	@Test
	public void driverCreateService() {
		final Object testingData[][] = {
			{
				//Positive without a comment
				"User1", "DomainService1", "Rendezvous1", null, "CreditCard1", null
			}, {
				//Positive with comment
				"User1", "DomainService1", "Rendezvous2", "comment", "CreditCard1", null
			}, {
				//The rendezvous already has this request
				"User1", "DomainService1", "Rendezvous4", "coment", "CreditCard1", IllegalArgumentException.class
			}, {
				//The rendezvous doesn't belong to the user
				"User2", "DomainService1", "Rendezvous3", "coment", "CreditCard1", IllegalArgumentException.class
			}, {
				//The credit card doesn't belong to the user
				"User1", "DomainService1", "Rendezvous5", "coment", "CreditCard2", IllegalArgumentException.class
			}, {
				//The service has been cancelled
				"User1", "DomainService7", "Rendezvous6", "coment", "CreditCard1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void templateCreate(final String username, final String servicePopulate, final String rendezvousPopulate, final String comment, final String creditCardPopulate, final Class<?> expected) {
		Class<?> caught;
		final Request request;
		caught = null;

		try {
			super.authenticate(username);

			request = this.requestService.create();

			final DomainService service = this.serviceService.findOne(super.getEntityId(servicePopulate));
			final CreditCard creditCard = this.creditCardService.findOne(super.getEntityId(creditCardPopulate));

			final int rendezvousId = super.getEntityId(rendezvousPopulate);

			request.setService(service);
			request.setCreditCard(creditCard);
			request.setComment(comment);

			this.requestService.saveNewRequest(request, rendezvousId);
			this.requestService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
