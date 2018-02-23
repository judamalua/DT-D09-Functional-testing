
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private Validator				validator;


	// Simple CRUD methods --------------------------------------------------

	public Administrator create() {
		Administrator result;

		result = new Administrator();

		return result;
	}

	public Collection<Administrator> findAll() {

		Collection<Administrator> result;

		Assert.notNull(this.administratorRepository);
		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Administrator findOne(final int administratorId) {

		Administrator result;

		result = this.administratorRepository.findOne(administratorId);

		return result;

	}

	public Administrator save(final Administrator administrator) {

		assert administrator != null;

		Administrator result;

		result = this.administratorRepository.save(administrator);

		return result;

	}

	public void delete(final Administrator administrator) {

		assert administrator != null;
		assert administrator.getId() != 0;

		Assert.isTrue(this.administratorRepository.exists(administrator.getId()));

		this.administratorRepository.delete(administrator);

	}

	// Other business methods

	public Administrator reconstruct(final Administrator admin, final BindingResult binding) {
		Administrator result;

		if (admin.getId() == 0) {

			UserAccount userAccount;
			Collection<Authority> authorities;
			Authority authority;

			userAccount = admin.getUserAccount();
			authorities = new HashSet<Authority>();
			authority = new Authority();

			result = admin;

			authority.setAuthority(Authority.ADMIN);
			authorities.add(authority);
			userAccount.setAuthorities(authorities);

		} else {
			result = this.administratorRepository.findOne(admin.getId());

			result.setName(admin.getName());
			result.setSurname(admin.getSurname());
			result.setPostalAddress(admin.getPostalAddress());
			result.setPhoneNumber(admin.getPhoneNumber());
			result.setEmail(admin.getEmail());
			result.setBirthDate(admin.getBirthDate());
		}
		this.validator.validate(result, binding);

		return result;
	}
}
