package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ConfigurationRepository;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ConfigurationRepository	configurationRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Configuration create() {
		Configuration result;

		result = new Configuration();

		return result;
	}

	public Collection<Configuration> findAll() {

		Collection<Configuration> result;

		Assert.notNull(this.configurationRepository);
		result = this.configurationRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Configuration findOne(final int configurationId) {

		Configuration result;

		result = this.configurationRepository.findOne(configurationId);

		return result;

	}

	public Configuration save(final Configuration configuration) {

		assert configuration != null;

		Configuration result;

		result = this.configurationRepository.save(configuration);

		return result;

	}

	public void delete(final Configuration configuration) {

		assert configuration != null;
		assert configuration.getId() != 0;

		Assert.isTrue(this.configurationRepository.exists(configuration.getId()));

		this.configurationRepository.delete(configuration);

	}
}

