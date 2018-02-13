package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;


	// Supporting services --------------------------------------------------

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
}

