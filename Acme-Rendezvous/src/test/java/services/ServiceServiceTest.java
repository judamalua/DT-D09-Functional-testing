
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.DomainService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ServiceServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private ServiceService			serviceService;
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private UserService				userService;


	// Tests ------------------------------------------------------------------

	/**
	 * This driver checks several tests regarding functional requirement number 4.2: An actor who is authenticated as a user must be
	 * able to list the services that are available in the system, and functional requirement number 5.1: An actor who is authenticated as a manager must be
	 * able to list the services that are available in the system. Every test is explained inside
	 * 
	 * @author Juanmi
	 */
	@Test
	public void driverListServices() {
		final Object testingData[][] = {
			{
				// This test checks that authenticated users can list services
				"User1", null
			}, {
				// This test checks that authenticated users can list services
				"Manager1", null
			}, {
				// This test checks that unauthenticated users cannot list services
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListServices((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Ancillary methods ---------------------------------------------------------------------------------------

	protected void templateListServices(final String username, final Class<?> expected) {
		Class<?> caught;
		Collection<DomainService> services;
		Page<DomainService> pageServices;
		Pageable pageable;

		caught = null;

		try {
			pageable = new PageRequest(0, this.configurationService.findConfiguration().getPageSize());

			super.authenticate(username);
			pageServices = this.serviceService.findNotCancelledServices(pageable);
			services = pageServices.getContent();

			Assert.notNull(services);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
