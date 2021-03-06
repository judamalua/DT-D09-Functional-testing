
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private Validator			validator;


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

		creditCard = request.getCreditCard();
		now = new Date(System.currentTimeMillis() - 10);
		rendezvous = this.rendezvousService.findOne(rendezvousId);

		// Checks that the User connected is the owner of the Rendezvous
		this.checkRendezvousBelongsToPrincipal(rendezvousId);

		// Checks that there isn't already a request between the same Service and Rendezvous.
		this.checkServiceNotAlreadyRequestedByRendezvous(request.getService(), rendezvous);

		// Checks that the credit card that is being used belongs to the principal
		this.creditCardService.checkCreditCardBelongsToPrincipal(creditCard);

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
	 * This method checks that the Request is not being made to a Service that has already been requested
	 * by that Rendezvous.
	 * 
	 * @param service
	 * @param rendezvous
	 * @author Antonio
	 */
	private void checkServiceNotAlreadyRequestedByRendezvous(final DomainService service, final Rendezvous rendezvous) {
		Collection<DomainService> servicesByRendezvous;

		//We obtain the list of Services requested by the Rendezvous
		servicesByRendezvous = this.serviceService.getServicesRequestedFromRendezvous(rendezvous.getId());

		//We assert that the Service hasn't already been requested by that Rendezvous.
		Assert.isTrue(!servicesByRendezvous.contains(service));

	}

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

	/**
	 * Returns a Collection of all the Requests made by the principal, who must be an User.
	 * 
	 * @return Collection<Request>
	 * @author Antonio
	 */
	public Collection<Request> getAllRequestFromUserPrincipal() {
		Collection<Request> result;
		User user;

		user = (User) this.actorService.findActorByPrincipal();
		result = this.requestRepository.getAllRequestFromUserPrincipal(user);

		return result;
	}

	/**
	 * Returns a Collection of all the Requests that were paid by the CreditCard passed as a param.
	 * 
	 * @return Collection<Request>
	 * @param creditCardId
	 * @author Antonio
	 */
	public Collection<Request> getAllRequestFromCreditCard(final int creditCardId) {
		Collection<Request> result;

		result = this.requestRepository.getAllRequestFromCreditCard(creditCardId);

		return result;
	}

	public void flush() {
		this.requestRepository.flush();
	}

	/**
	 * Given a request id, it'll return the rendezvous of that request, just the manager of the service of the request can acess this data
	 * 
	 * @param requestId
	 *            The request to find
	 * @author Daniel Diment
	 * @return
	 *         The found rendezvous
	 */
	public Rendezvous findRendezvousByRequestId(final int requestId) {
		final DomainService service = this.requestRepository.findOne(requestId).getService();
		//Tests that the actor accessing this rendezvous is the manager of the service
		Assert.isTrue(this.managerService.findManagerByService(service).getId() == this.actorService.findActorByPrincipal().getId());
		return this.requestRepository.findRendezvousByRequestId(requestId);
	}

	public Request reconstruct(final Request request, final BindingResult binding) {
		Request result;
		User user;
		Date moment;
		CreditCard creditCard;

		moment = new Date(System.currentTimeMillis() - 10);
		creditCard = request.getCreditCard();

		if (request.getId() == 0) {
			request.setMoment(moment);

			if (creditCard.getId() == 0) {
				user = (User) this.actorService.findActorByPrincipal();
				creditCard.setUser(user);

				request.setCreditCard(creditCard);
			} else {
				creditCard = this.creditCardService.findOne(creditCard.getId());

				request.setCreditCard(creditCard);
			}

			result = request;
		} else
			result = this.requestRepository.findOne(request.getId());

		this.validator.validate(result, binding);
		return result;
	}
}
