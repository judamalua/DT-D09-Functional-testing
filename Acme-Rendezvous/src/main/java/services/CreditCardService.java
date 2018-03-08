
package services;

import java.util.Collection;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.CreditCard;
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
		String res = "";
		Random random;
		random = new Random();

		final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgzijklmnopqrstuvwxyz0123456789";

		for (int i = 0; i < 14; i++)
			res += alphabet.charAt(random.nextInt(alphabet.length()));

		if (this.creditCardRepository.getAllCookieTokens().contains(res.toString()))
			return this.generateCookieToken();
		else
			return res;
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
		Assert.isTrue(result.getUser().getId() == this.actorService.findActorByPrincipal().getId());
		return result;
	}
}
