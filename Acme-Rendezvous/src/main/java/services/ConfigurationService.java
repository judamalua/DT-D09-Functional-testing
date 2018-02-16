
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

	/**
	 * Create a new Configuration entity
	 * 
	 * @return The Configuration entity
	 * @author MJ
	 */
	public Configuration create() {
		Configuration result;

		result = new Configuration();

		return result;
	}

	/**
	 * Gets all the configurations in the system
	 * 
	 * @return the configurations in the system
	 * @author MJ
	 */
	public Collection<Configuration> findAll() {

		Collection<Configuration> result;

		Assert.notNull(this.configurationRepository);
		result = this.configurationRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * 
	 * Gets the configuration with id configurationId
	 * 
	 * @param configurationId
	 * @return the configuration with id equals to the param
	 * @author MJ
	 */
	public Configuration findOne(final int configurationId) {

		Configuration result;

		result = this.configurationRepository.findOne(configurationId);

		return result;

	}

	/**
	 * 
	 * Saves a configuration in the system
	 * 
	 * @param configuration
	 * @return The configuration saved
	 * @author MJ
	 */
	public Configuration save(final Configuration configuration) {

		assert configuration != null;

		Configuration result;

		result = this.configurationRepository.save(configuration);

		return result;

	}

	/**
	 * Delete the configuration passed as parameter
	 * 
	 * @param configuration
	 * @author MJ
	 */
	public void delete(final Configuration configuration) {

		assert configuration != null;
		assert configuration.getId() != 0;

		Assert.isTrue(this.configurationRepository.exists(configuration.getId()));

		this.configurationRepository.delete(configuration);

	}

	/**
	 * Gets the unique configuration in the system
	 * 
	 * @return the configuratio of the system
	 * @author MJ
	 */
	public Configuration findConfiguration() {
		Configuration result;

		result = this.configurationRepository.findConfiguration();
		Assert.notNull(result);

		return result;

	}
}
