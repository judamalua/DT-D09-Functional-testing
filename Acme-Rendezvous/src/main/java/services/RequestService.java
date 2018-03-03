
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.CreditCard;
import domain.DomainService;
import domain.Rendezvous;
import domain.Request;
import domain.User;

@Service
@Transactional
public class RequestService {

	// Managed repository --------------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;


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
	 * This method returns a new request with its service instantiated
	 * 
	 * @param serviceId
	 * @return Request
	 * @author Antonio
	 */
	public Request createByService(final int serviceId) {
		Request result;
		Date now;
		DomainService service;

		service = this.serviceService.findOne(serviceId);
		result = new Request();
		now = new Date(System.currentTimeMillis() - 10);

		result.setMoment(now);
		result.setService(service);

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
	 * This method saves a request passed as a param into the database. It also refresh the
	 * requests list of the rendezvous, and saves the credit card into the system.
	 * 
	 * @param request
	 *            , rendezvousId
	 * @return Request
	 * @author Antonio
	 */
	public Request saveNewRequest(final Request request, final int rendezvousId) {
		Assert.notNull(request);
		Assert.notNull(request.getCreditCard());
		Assert.isTrue(rendezvousId != 0);

		Request result;
		Date now;
		Rendezvous rendezvous;
		CreditCard creditCard;

		//Checks that the User connected is the owner of the rendezvous
		this.checkRendezvousBelongsToPrincipal(rendezvousId);

		rendezvous = this.rendezvousService.findOne(rendezvousId);

		now = new Date(System.currentTimeMillis() - 10);
		creditCard = request.getCreditCard();

		creditCard = this.creditCardService.save(creditCard);

		request.setMoment(now);
		request.setCreditCard(creditCard);

		result = this.requestRepository.save(request);

		rendezvous.getRequests().add(result);
		this.rendezvousService.save(rendezvous);

		return result;
	}

	/**
	 * This method saves a request passed as a param into the system.
	 * 
	 * @param request
	 * @return Request
	 * @author Antonio
	 */
	public Request save(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);

		Request result;

		result = this.requestRepository.save(request);

		return result;
	}

	/**
	 * This method deletes a request made by an User, and it also deletes the credit card used
	 * for that request from the system.
	 * 
	 * @param request
	 * @author Antonio
	 */
	public void delete(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);

		CreditCard creditCard;

		creditCard = request.getCreditCard();

		this.requestRepository.delete(request);

		this.creditCardService.delete(creditCard);
	}

	//Business rule methods -------------------------------------------------------------

	/**
	 * This method checks that the user connected to the system (the principal) is the owner of
	 * the rendezvous from he wants to make the request.
	 * 
	 * @param rendezvousId
	 * @author Antonio
	 */
	private void checkRendezvousBelongsToPrincipal(final int rendezvousId) {
		User userPrincipal, rendezvousOwner;

		userPrincipal = (User) this.actorService.findActorByPrincipal();
		rendezvousOwner = this.userService.getCreatorUser(rendezvousId);

		Assert.isTrue(userPrincipal.equals(rendezvousOwner));
	}

	public Collection<Request> getAllRequestFromUserPrincipal() {
		Collection<Request> result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();
		result = this.requestRepository.getAllRequestFromUserPrincipal(user);

		return result;
	}
}
