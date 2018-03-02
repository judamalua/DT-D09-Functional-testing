
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed repository --------------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------
	/**
	 * This method returns a new request, with all of its attributes empty
	 * 
	 * 
	 * @return Request
	 * @author Antonio
	 */
	public Request create() {
		Request result;
		Date now;

		result = new Request();
		now = new Date(System.currentTimeMillis() - 10);
		result.setMoment(now);

		return result;
	}

	/**
	 * This method returns a collection of all the requests in the system.
	 * 
	 * 
	 * @return Collection<Request>
	 * @author Antonio
	 */
	public Collection<Request> findAll() {
		Collection<Request> result;

		Assert.notNull(this.requestRepository);

		result = this.requestRepository.findAll();

		Assert.notNull(result);

		return result;
	}

	/**
	 * This method returns the request which ID is passed by means of a param.
	 * 
	 * @param requestId
	 * @return Request
	 * @author Antonio
	 */
	public Request findOne(final int requestId) {
		Assert.isTrue(requestId != 0);

		Request result;

		result = this.requestRepository.findOne(requestId);

		return result;
	}

	/**
	 * This method saves a request passed as a param into the database.
	 * 
	 * @param request
	 * @return Request
	 * @author Antonio
	 */
	public Request save(final Request request) {
		Assert.notNull(request);
		Assert.notNull(request.getCreditCard());

		Request result;
		Date now;

		now = new Date(System.currentTimeMillis() - 10);
		request.setMoment(now);

		result = this.requestRepository.save(request);

		return result;
	}

	//This service has no delete option
}
