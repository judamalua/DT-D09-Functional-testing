
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.DomainService;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CreditCardServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private CreditCardService	creditCardService;
	@Autowired
	private ServiceService		serviceService;
	@Autowired
	private RequestService		requestService;


	// Tests ------------------------------------------------------------------
	/**
	 * This test checks that the CreditCard that authenticated users use to request a Service
	 * for one of their created Rendezvouses is valid, as said in the Functional Requirement n. 4.3:
	 * Request a service for one of the rendezvouses that he or she's created.
	 * "He or she must specify a valid credit card" in every request for a service*.
	 * Optionally, he or she can provide some comments in the request.
	 * 
	 * @author Antonio
	 */
	public void testValidCreditCard() {
		int serviceId;
		DomainService service;
		Request request;
		CreditCard creditCard, savedCreditCard;

		super.authenticate("user1");
		serviceId = super.getEntityId("DomainService6");
		service = this.serviceService.findOne(serviceId);

		request = this.requestService.createByService(serviceId);
		creditCard = this.creditCardService.create();

		creditCard.setHolderName("Valid Holder Name");//Valid Holder name
		creditCard.setBrandName("Valid Brand Name"); //Valid Brand name
		creditCard.setNumber("4485677312398507"); //Valid Credit Card number.
		creditCard.setCvv(123); //Valid CVV
		creditCard.setExpirationMonth(12); //Valid Expiration Month.
		creditCard.setExpirationYear(20); // Valid Expiration Year.

		savedCreditCard = this.creditCardService.save(creditCard);

		this.creditCardService.flush();

		Assert.notNull(this.creditCardService.findOne(savedCreditCard.getId()));

		super.unauthenticate();
	}

	@Test
	public void driver() {

		final Object testingData[][] = {//Username, HolderName, BrandName, Number, CVV, ExpirationYear, ExpirationMonth, ExpectedException
			{
				//Checks that the Holder name must not be blank.
				"user1", "", "Valid Brand Name", "4485677312398507", 123, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Positive test, an user can save a Valid CreditCard.
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, null
			}, {
				//Checks that you must be logged as an User to save a CreditCard.
				null, "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, IllegalArgumentException.class
			}, {
				//Checks that you must be logged as an User to save a CreditCard.
				"manager1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, java.lang.ClassCastException.class
			}, {
				//Checks that you must be logged as an User to save a CreditCard.
				"admin", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, java.lang.ClassCastException.class
			}, {
				//Checks that the Brand name must not be blank.
				"user1", "Valid Holder Name", "", "4485677312398507", 123, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Number must not be blank.
				"user1", "Valid Holder Name", "Valid Brand Name", "", 123, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Number must be a valid Credit Card number.
				"user1", "Valid Holder Name", "Valid Brand Name", "1234567891234567", 123, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the CVV is not outside the minimum range (100).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 99, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the CVV is not outside the maximum range (999).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 1000, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the CVV can be the lowest int in the range (100).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 100, 12, 20, null
			}, {
				//Checks that the CVV can be the lowest int in the range + 1 (100).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 101, 12, 20, null
			}, {
				//Checks that the CVV can be the middle int in the range  (100 - 999).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 549, 12, 20, null
			}, {
				//Checks that the CVV can be the middle int in the range  (100 - 999).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 550, 12, 20, null
			}, {
				//Checks that the CVV can be the greatest int in the range - 1 (999).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 998, 12, 20, null
			}, {
				//Checks that the CVV can be the greatest int in the range (999).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 999, 12, 20, null
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			System.out.println("i = " + i + "\n");
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Class<?>) testingData[i][7]);
		}

	}
	protected void template(final String username, final String holderName, final String brandName, final String number, final int cvv, final int expirationMonth, final int expirationYear, final Class<?> expected) {
		CreditCard creditCard;
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);
			System.out.println("Holder Name procedente del Driver: " + holderName);

			creditCard = this.creditCardService.create();

			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setCvv(cvv);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);

			this.creditCardService.save(creditCard);

			super.unauthenticate();

			this.creditCardService.flush();
		} catch (final Throwable oops) {
			System.out.println("Excepcion en template: " + oops);
			System.out.println("Excepcion esperada: " + expected);

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
