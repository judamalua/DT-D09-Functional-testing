
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.CreditCard;
import domain.DomainService;
import domain.Manager;
import domain.Request;
import forms.ManagerForm;

@Service
@Transactional
public class ManagerService {

	// Managed repository --------------------------------------------------
	@Autowired
	private ManagerRepository	managerRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private Validator			validator;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private RequestService		requestService;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * That method creates an instance of a Manager.
	 * 
	 * @return Manager
	 * @author Antonio
	 */
	public Manager create() {
		Manager result;
		Collection<DomainService> services;
		Collection<Authority> authorities;
		Authority authority;
		UserAccount userAccount;

		result = new Manager();
		services = new ArrayList<DomainService>();
		userAccount = new UserAccount();
		authorities = new HashSet<Authority>();
		authority = new Authority();
		authority.setAuthority(Authority.MANAGER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		result.setServices(services);
		result.setUserAccount(userAccount);

		return result;
	}

	/**
	 * This method returns a collection with all of the managers of the system.
	 * 
	 * @return Collection<Manager>
	 * @author Antonio
	 */
	public Collection<Manager> findAll() {
		Collection<Manager> result;

		Assert.notNull(this.managerRepository);

		result = this.managerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	/**
	 * 
	 * This method returns the Manager which ID is passed by means of a param.
	 * 
	 * @param managerId
	 * @return Manager
	 * @author Antonio
	 */
	public Manager findOne(final int managerId) {
		Assert.isTrue(managerId != 0);

		Manager result;

		result = this.managerRepository.findOne(managerId);

		return result;
	}

	/**
	 * This method saves a manager passed as a param into the database.
	 * 
	 * @param manager
	 * @return Manager
	 * @author Antonio
	 */
	public Manager save(final Manager manager) {
		Assert.notNull(manager);

		Actor actor;

		actor = this.actorService.findActorByPrincipal();

		if (manager.getId() != 0 && actor instanceof Manager)
			Assert.isTrue(actor.equals(manager));

		Manager result;

		result = this.managerRepository.save(manager);

		return result;

	}
	// Other business methods ----------------------------------------------------------------

	/**
	 * This method reconstruct a manager, initializing him from scratch or getting his attributes from the DB.
	 * 
	 * @param manager
	 *            , the manager to be reconstructed
	 * @param binding
	 *            , where the possible errors are stored
	 * @return Manager
	 * @author Antonio
	 */
	public Manager reconstruct(final ManagerForm managerForm, final BindingResult binding) {
		Manager result;
		Collection<DomainService> services;

		if (managerForm.getId() == 0) {
			services = new HashSet<DomainService>();

			result = this.create();

			result.getUserAccount().setUsername(managerForm.getUserAccount().getUsername());
			result.getUserAccount().setPassword(managerForm.getUserAccount().getPassword());
			result.setVat(managerForm.getVat());
			result.setName(managerForm.getName());
			result.setSurname(managerForm.getSurname());
			result.setPostalAddress(managerForm.getPostalAddress());
			result.setPhoneNumber(managerForm.getPhoneNumber());
			result.setEmail(managerForm.getEmail());
			result.setBirthDate(managerForm.getBirthDate());

			result.setServices(services);

		} else {
			result = this.findOne(managerForm.getId());

			result.setName(managerForm.getName());
			result.setSurname(managerForm.getSurname());
			result.setPostalAddress(managerForm.getPostalAddress());
			result.setPhoneNumber(managerForm.getPhoneNumber());
			result.setEmail(managerForm.getEmail());
			result.setBirthDate(managerForm.getBirthDate());
			result.setVat(managerForm.getVat());

		}

		this.validator.validate(result, binding);

		return result;
	}

	/**
	 * This method deconstructs a Manager object, that is, transforms
	 * a Manager object into a UserAdminForm object to be edited
	 * 
	 * @param user
	 *            to be deconstructed into an UserAdminForm
	 * @return UserAdminForm with the data of the user given by parameters
	 * 
	 * @author Juanmi
	 */
	public ManagerForm deconstruct(final Manager manager) {
		ManagerForm result;

		result = new ManagerForm();

		result.setId(manager.getId());
		result.setVersion(manager.getVersion());
		result.setVat(manager.getVat());
		result.setName(manager.getName());
		result.setSurname(manager.getSurname());
		result.setPostalAddress(manager.getPostalAddress());
		result.setPhoneNumber(manager.getPhoneNumber());
		result.setEmail(manager.getEmail());
		result.setBirthDate(manager.getBirthDate());

		return result;
	}

	public Page<DomainService> findServicesByManager(final Manager manager, final Pageable pageable) {
		Page<DomainService> result;
		Assert.notNull(manager);
		Assert.notNull(pageable);

		result = this.managerRepository.findServicesByManager(manager.getId(), pageable);

		return result;

	}

	/**
	 * Returns the Manager that created the Service passed as a param.
	 * 
	 * @return Manager
	 * @param service
	 * @author Antonio
	 * 
	 */
	public Manager findManagerByService(final DomainService service) {
		Manager result;
		Assert.notNull(service);

		result = this.managerRepository.findManagerByService(service.getId());

		return result;

	}

	/**
	 * This method do a flush in database
	 * 
	 * 
	 * @author Luis
	 */
	public void flush() {
		this.managerRepository.flush();

	}

	/**
	 * Checks that the Manager (the principal) has access to this CreditCard, that is,
	 * the CreditCard is part of a Request to one of the Services that the Manager offers.
	 * 
	 * @param creditCard
	 * @author Antonio
	 * 
	 */
	public void checkManagerCreditCard(final CreditCard creditCard) {
		DomainService service;
		Collection<Request> requests;
		Boolean hasAccess;
		Manager principal;
		Collection<DomainService> principalServices;

		hasAccess = false; //First, the Manager doesn't have access.
		requests = this.requestService.getAllRequestFromCreditCard(creditCard.getId()); //List of requests that were paid by the CreditCard
		principal = (Manager) this.actorService.findActorByPrincipal();//The Manager who wants to access.
		principalServices = principal.getServices(); //The Services of the principal

		for (final Request r : requests) { //We iterate the Requests paid by the CreditCard
			service = r.getService();//We get the service of the Request

			if (principalServices.contains(service)) { //If one of those Services is cointained in the list of Services offered by the Manager,
				hasAccess = true;//We grant access.
				break;
			}
		}

		Assert.isTrue(hasAccess);

	}

}
