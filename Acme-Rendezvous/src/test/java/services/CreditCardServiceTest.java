
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CreditCardServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private CreditCardService	creditCardService;


	// Tests ------------------------------------------------------------------
	/**
	 * This driver contains a number of valid and invalids Credit Cards, and with the help of the template
	 * method, it tests whether they can be saver or not, depending on the errors (or not) of each
	 * Credit Card of the Array of Arrays 'testingData[][]'.
	 * Tests Functional Requirement 4.3: An actor who is authenticated as a user must be able to:
	 * Request a service for one of the rendezvouses that he or she's created. He or she
	 * must specify a VALID CREDIT CARD in every request for a service. Optionally, he or she
	 * can provide some comments in the request.
	 * 
	 */
	@Test
	public void driver() {

		final Object testingData[][] = {//Username, HolderName, BrandName, Number, CVV, ExpirationMonth, ExpirationYear, ExpectedException
			{
				//Positive test, an user can save a Valid CreditCard.
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, null
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
			}, {
				//Checks that you must be logged as an User to save a CreditCard.
				null, "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, IllegalArgumentException.class
			}, {
				//Checks that the Expiration Month can be the lowest value in range (1).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 1, 20, null
			}, {
				//Checks that the Expiration Month can be the lowest value in range +1 (1).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 2, 20, null
			}, {
				//Checks that the Expiration Month can be the middle value in range  (6).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 6, 20, null
			}, {
				//Checks that the Expiration Month can be the greatest value in range -1  (12).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 11, 20, null
			}, {
				//Checks that the Expiration Month can be the greatest value in range   (12).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, null
			}, {
				//Checks that the Expiration Year can be the lowest value, acording to the actual year (18).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 18, null
			}, {
				//Checks that the Expiration Year can be the lowest value +1(00).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 19, null
			}, {
				//Checks that the Expiration Year can be the middle value in range (50).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 50, null
			}, {
				//Checks that the Expiration Year can be the greatest value -1 (99).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 98, null
			}, {
				//Checks that the Expiration Year can be the greatest value  (99).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 99, null
			}, {
				//Checks that you must be logged as an User to save a CreditCard.
				"manager1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, java.lang.ClassCastException.class
			}, {
				//Checks that you must be logged as an User to save a CreditCard.
				"admin", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, java.lang.ClassCastException.class
			}, {
				//Checks that the Holder name must not be blank.
				"user1", "", "Valid Brand Name", "4485677312398507", 123, 12, 20, javax.validation.ConstraintViolationException.class
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
				//Checks that the Expiration Month is not outside the minimum range (1).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 0, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Expiration Month is not outside the maximum range (12).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 13, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Expiration Year is not outside the minimum range (0).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, -1, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Expiration Year is not outside the maximum range (99).
				"user1", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 100, javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Class<?>) testingData[i][7]);

	}

	/**
	 * This template method receives the required params to create and save a new CreditCard, and
	 * if an exception is thrown, it checks if it is expected. If not, the jUnit test fails.
	 * 
	 * @param username
	 *            the username of the actor logged
	 * @param holderName
	 *            the holderName of the CreditCard
	 * @param brandName
	 *            the brandName of the CreditCard
	 * @param number
	 *            the number of the CreditCard
	 * @param cvv
	 *            the CVV of the CreditCard
	 * @param expirationMonth
	 *            the expirationMonth of the CreditCard
	 * @param expirationYear
	 *            the expirationYear of the CreditCard
	 * @param expected
	 *            the exception expected to be thrown
	 */
	protected void template(final String username, final String holderName, final String brandName, final String number, final int cvv, final int expirationMonth, final int expirationYear, final Class<?> expected) {
		CreditCard creditCard;
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);

			creditCard = this.creditCardService.create();

			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setCvv(cvv);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);

			this.creditCardService.save(creditCard);

			this.creditCardService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * This driver has an Array of Arrays called 'testingData' which contains a number of combinations
	 * of usernames and expected exceptions, and from each tuple calls the templateUsersHaveAccessToCreditCard method
	 * to test if the user can access to the information of a provided CreditCard.
	 */
	@Test
	public void driverUsersHaveAccessToCreditCard() {
		final Object testingData[][] = {
			{
				"user1", null
			}, {
				"user2", IllegalArgumentException.class
			}, {
				"user3", IllegalArgumentException.class
			}, {
				"user4", IllegalArgumentException.class
			}, {
				"user5", IllegalArgumentException.class
			}, {
				"user6", IllegalArgumentException.class
			}, {
				"user7", IllegalArgumentException.class
			}, {
				"user8", IllegalArgumentException.class
			}, {
				"manager1", java.lang.ClassCastException.class
			}, {
				"manager2", java.lang.ClassCastException.class
			}, {
				"manager3", java.lang.ClassCastException.class
			}, {
				"manager4", java.lang.ClassCastException.class
			}, {
				"manager5", java.lang.ClassCastException.class
			}, {
				"manager6", java.lang.ClassCastException.class
			}, {
				"manager7", java.lang.ClassCastException.class
			}, {
				"manager8", java.lang.ClassCastException.class
			}, {
				"admin", java.lang.ClassCastException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUsersHaveAccessToCreditCard((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * This template logs the username into the system and tries to access to a CreditCard to obtain
	 * its information. If the User doesn't have access to that CreditCard, then an exception is
	 * expected to happen.
	 * 
	 * @param username
	 *            , the principal.
	 * @param expected
	 *            , the exception expected to happen.
	 */
	protected void templateUsersHaveAccessToCreditCard(final String username, final Class<?> expected) {
		CreditCard creditCard;
		Integer creditCardId;
		Class<?> caught;

		caught = null;
		creditCardId = super.getEntityId("CreditCard1");
		creditCard = this.creditCardService.findOne(creditCardId);

		try {
			super.authenticate(username);

			this.creditCardService.checkUserCreditCard(creditCard);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}
}
