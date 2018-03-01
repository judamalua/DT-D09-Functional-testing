
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.Authority;
import security.UserAccount;
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
}
