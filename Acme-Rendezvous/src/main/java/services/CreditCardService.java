
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

	// Managed repository --------------------------------------------------
	@Autowired
	private CreditCardRepository	creditCardRepository;


	// Supporting services --------------------------------------------------

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

}
