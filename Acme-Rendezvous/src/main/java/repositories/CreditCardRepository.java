
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

	/**
	 * Gets all the cookie tokens to test if the token is repeated or not
	 * 
	 * @author Daniel Diment
	 * @return The list of all the tokens
	 */
	@Query("select c.cookieToken from CreditCard c")
	Collection<String> getAllCookieTokens();

}
