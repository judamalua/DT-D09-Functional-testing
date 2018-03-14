
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.User;

@Service
@Transactional
public class CreditCardService {

	// Managed repository --------------------------------------------------
	@Autowired
	private CreditCardRepository	creditCardRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private UserService				userService;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * This method returns a new credit card, with all of its attributes empty
	 * 
	 * 
	 * @return CreditCard
	 * @author Antonio
	 */
	public CreditCard create() {
		CreditCard result;

		result = new CreditCard();

		result.setCookieToken(this.generateCookieToken());

		result.setUser((User) this.actorService.findActorByPrincipal());

		return result;
	}

	/**
	 * This method returns a collection of all the credit cards in the system.
	 * 
	 * 
	 * @return Collection<CreditCard>
	 * @author Antonio
	 */
	public Collection<CreditCard> findAll() {
		Collection<CreditCard> result;

		Assert.notNull(this.creditCardRepository);

		result = this.creditCardRepository.findAll();

		Assert.notNull(result);

		return result;
	}

	/**
	 * This method returns the credit card which ID is passed by means of a param.
	 * 
	 * @param creditCardId
	 * @return CreditCard
	 * @author Antonio
	 */
	public CreditCard findOne(final int creditCardId) {
		Assert.isTrue(creditCardId != 0);

		CreditCard result;

		result = this.creditCardRepository.findOne(creditCardId);

		return result;
	}

	/**
	 * This method saves a credit card passed as a param into the database.
	 * 
	 * @param creditCard
	 * @return CreditCard
	 * @author Antonio
	 */
	public CreditCard save(final CreditCard creditCard) {
		Assert.notNull(creditCard);

		CreditCard result;

		//Checks that the CreditCard hasn't expired
		this.checkCreditCardExpired(creditCard);
		creditCard.setUser((User) this.actorService.findActorByPrincipal());
		result = this.creditCardRepository.save(creditCard);

		return result;
	}

	/**
	 * This method deletes a credit card passed as a param from the database.
	 * 
	 * @param creditCard
	 * @author Antonio
	 */
	public void delete(final CreditCard creditCard) {
		Assert.notNull(creditCard);
		Assert.isTrue(creditCard.getId() != 0);

		this.creditCardRepository.delete(creditCard);

	}

	/**
	 * Generates an unique and random cookie token for every credit card
	 * 
	 * @author Daniel Diment
	 * @return
	 *         The random token
	 */
	private String generateCookieToken() {
		String alphabet, result;
		Random random;
		StringBuilder stringBuilder;

		random = new Random();
		stringBuilder = new StringBuilder();
		alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgzijklmnopqrstuvwxyz0123456789";

		for (int i = 0; i < 14; i++)
			stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));

		result = stringBuilder.toString();
		if (this.creditCardRepository.getAllCookieTokens().contains(result))
			result = this.generateCookieToken();

		return result;
	}
	/**
	 * Finds a credit card by its token. If the user is not the owner of the credit card the search will fail.
	 * 
	 * @param cookieToken
	 *            The token to find by
	 * @author Daniel Diment
	 * @return
	 *         The credit card
	 */
	public CreditCard findByCookieToken(final String cookieToken) {
		final CreditCard result = this.creditCardRepository.findByCookieToken(cookieToken);
		//Checks that the CreditCard hasn't expired
		this.checkCreditCardExpired(result);
		Assert.isTrue(result.getUser().getId() == this.actorService.findActorByPrincipal().getId());
		return result;
	}

	/**
	 * Checks that the User (the principal) has access to this CreditCard, that is,
	 * the CreditCard is part of a Request by one of the Rendezvouses that the User has created.
	 * 
	 * @param creditCard
	 * @author Antonio
	 * 
	 */
	public void checkUserCreditCard(final CreditCard creditCard) {
		User principal, ownerCreditCard;
		Collection<Request> requests;
		Rendezvous rendezvous;
		Boolean hasAccess;

		principal = (User) this.actorService.findActorByPrincipal();
		requests = this.requestService.getAllRequestFromCreditCard(creditCard.getId());
		hasAccess = false;

		for (final Request r : requests) {
			rendezvous = this.rendezvousService.findRendezvousByRequest(r.getId());
			ownerCreditCard = this.userService.getCreatorUser(rendezvous.getId());

			if (ownerCreditCard.equals(principal)) {
				hasAccess = true;
				break;
			}
		}

		Assert.isTrue(hasAccess);
	}

	/**
	 * This method checks that the Credit Card of the Request hasn't expired, checking its expiration
	 * year and expiration month.
	 * 
	 * @param creditCard
	 * @author Antonio
	 */
	public void checkCreditCardExpired(final CreditCard creditCard) {
		Integer actualMonth, actualYear, ccMonth, ccYear;
		DateFormat dfYear, dfMonth;
		String formattedYear, formattedMonth;

		ccMonth = creditCard.getExpirationMonth();
		ccYear = creditCard.getExpirationYear();

		dfYear = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		formattedYear = dfYear.format(Calendar.getInstance().getTime());
		actualYear = Integer.valueOf(formattedYear);

		dfMonth = new SimpleDateFormat("MM"); //Just the month
		formattedMonth = dfMonth.format(Calendar.getInstance().getTime());
		actualMonth = Integer.valueOf(formattedMonth);

		//Asserts that the CreditCard expiration Year is greater than the actual year
		Assert.isTrue(ccYear >= actualYear, "CreditCard expiration Date error");

		//If the CreditCard expiration Year is the same that the actual Year, 
		//Asserts that the CreditCard expiration Month is greater than the actual Month.
		if (ccYear == actualYear)
			Assert.isTrue(ccMonth > actualMonth, "CreditCard expiration Date error");

	}

	/**
	 * This method flushes the repository, this forces the cache to be saved to the database, which then forces the test data to be validated. This is only used
	 * in tests
	 * 
	 * @author Antonio
	 */
	public void flush() {
		this.creditCardRepository.flush();
	}
}
