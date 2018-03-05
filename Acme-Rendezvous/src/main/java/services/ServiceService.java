
package services;

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

import repositories.ServiceRepository;
import domain.Actor;
import domain.Category;
import domain.DomainService;
import domain.Manager;
import domain.Request;

@Service
@Transactional
public class ServiceService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ServiceRepository	serviceRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods --------------------------------------------------
	/**
	 * Create a new instance of an Service
	 * 
	 * @return a new instance of Service
	 * @author MJ
	 */
	public DomainService create() {
		DomainService result;

		result = new DomainService();

		return result;
	}

	/**
	 * Get all the entities of Service in the database
	 * 
	 * @return a collection of Service saved in the database
	 * @author MJ
	 */
	public Collection<DomainService> findAll() {

		Collection<DomainService> result;

		Assert.notNull(this.serviceRepository);
		result = this.serviceRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * Get the Service with id serviceId
	 * 
	 * @param serviceId
	 * @return the Service with the id passed as parameter
	 * @author MJ
	 */
	public DomainService findOne(final int serviceId) {

		DomainService result;

		result = this.serviceRepository.findOne(serviceId);
		Assert.isTrue(!result.getCancelled());

		return result;

	}

	/**
	 * Saves the Service in the database
	 * 
	 * @param service
	 * @return the saved Service in the database
	 * @author MJ
	 */
	public DomainService save(final DomainService service) {

		assert service != null;

		DomainService result;
		Manager manager;
		Actor actor;

		result = this.serviceRepository.save(service);

		actor = this.actorService.findActorByPrincipal();

		if (actor instanceof Manager) {
			manager = (Manager) this.actorService.findActorByPrincipal();
			if (service.getId() != 0)
				Assert.isTrue(manager.getServices().contains(service));
		} else
			manager = this.managerService.findManagerByService(service);

		//Updating manager
		manager.getServices().remove(service);
		manager.getServices().add(result);
		this.managerService.save(manager);

		//Updating categories
		for (final Category category : new HashSet<Category>(service.getCategories())) {
			if (category.getServices().contains(service))
				category.getServices().remove(service);
			category.getServices().add(result);
			this.categoryService.save(category);
		}

		return result;
	}

	/**
	 * Delete from database the Service passed as parameter
	 * 
	 * @param service
	 * @author MJ
	 */
	public void delete(final DomainService service) {

		assert service != null;
		assert service.getId() != 0;

		Assert.isTrue(service.getRequests().size() == 0);
		Assert.isTrue(this.serviceRepository.exists(service.getId()));

		Manager manager;

		manager = (Manager) this.actorService.findActorByPrincipal();
		Assert.isTrue(manager.getServices().contains(service));

		//Updating categories
		for (final Category category : new HashSet<Category>(service.getCategories())) {
			category.getServices().remove(service);
			this.categoryService.save(category);
		}

		//Updating manager
		manager.getServices().remove(service);
		this.managerService.save(manager);

		this.serviceRepository.delete(service);

	}

	/**
	 * Gets a page of not cancelled services
	 * 
	 * @param pageable
	 * @return the services not cancelled
	 * @author MJ
	 */
	public Page<DomainService> findNotCancelledServices(final Pageable pageable) {
		Assert.notNull(pageable);

		Page<DomainService> result;

		result = this.serviceRepository.findNotCancelledServices(pageable);

		return result;
	}

	// Other business methods

	/**
	 * Recontruct the Service whith his errors in the binding if exists
	 * 
	 * @param service
	 * @param binding
	 * @return The service reconstructed
	 * @author MJ
	 */
	public DomainService reconstruct(final DomainService service, final BindingResult binding) {
		DomainService result = null;

		if (service.getId() == 0) {

			Collection<Request> requests;
			Collection<Category> categories;

			requests = new HashSet<Request>();
			categories = new HashSet<Category>();

			result = service;

			result.setRequests(requests);

			if (result.getCategories() == null)
				result.setCategories(categories);

		} else {
			result = this.serviceRepository.findOne(service.getId());

			result.setName(service.getName());
			result.setDescription(service.getDescription());
			result.setPictureUrl(service.getPictureUrl());
			result.setPrice(service.getPrice());
			result.setCategories(service.getCategories());
		}
		this.validator.validate(result, binding);

		return result;
	}
}
