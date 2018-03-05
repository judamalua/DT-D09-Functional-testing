
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
import domain.DomainService;
import domain.Manager;

@Service
@Transactional
public class ManagerService {

	// Managed repository --------------------------------------------------
	@Autowired
	private ManagerRepository	managerRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private Validator			validator;


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

		Manager result;

		result = this.managerRepository.save(manager);

		return result;

	}

	// Other business methods ----------------------------------------------------------------
	public Manager reconstruct(final Manager manager, final BindingResult binding) {
		Manager result;
		UserAccount userAccount;
		Collection<Authority> authorities;
		Authority authority;
		Collection<DomainService> services;

		if (manager.getId() == 0) {
			userAccount = manager.getUserAccount();
			authorities = new HashSet<Authority>();
			authority = new Authority();
			authority.setAuthority(Authority.MANAGER);
			authorities.add(authority);
			userAccount.setAuthorities(authorities);

			services = new HashSet<DomainService>();

			result = manager;

			result.setServices(services);

		} else {
			result = this.findOne(manager.getId());

			result.setName(manager.getName());
			result.setSurname(manager.getSurname());
			result.setPostalAddress(manager.getPostalAddress());
			result.setPhoneNumber(manager.getPhoneNumber());
			result.setEmail(manager.getEmail());
			result.setBirthDate(manager.getBirthDate());
			result.setVat(manager.getVat());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public Page<DomainService> findServicesByManager(final Manager manager, final Pageable pageable) {
		Page<DomainService> result;
		Assert.notNull(manager);
		Assert.notNull(pageable);

		result = this.managerRepository.findServicesByManager(manager.getId(), pageable);

		return result;

	}

	public Manager findManagerByService(final DomainService service) {
		Manager result;
		Assert.notNull(service);

		result = this.managerRepository.findManagerByService(service.getId());

		return result;

	}
}
