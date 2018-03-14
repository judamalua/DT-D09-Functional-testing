
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
import domain.Manager;

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
	private ManagerService			managerService;


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

	/**
	 * This test checks that a manager can list the services he or she created, regarding functional requirement 5.2: An actor who is registered as a
	 * manager must be able to manage his or her services, which includes listing them, creating them, updating them, and deleting them as long as they are not
	 * required by any rendezvouses
	 * 
	 * @author Juanmi
	 */
	@Test
	public void testManagerListCreatedServices() {
		Manager manager;
		int managerId;
		Collection<DomainService> services;

		managerId = super.getEntityId("Manager1");
		manager = this.managerService.findOne(managerId);

		super.authenticate("Manager1");

		services = manager.getServices();

		Assert.notNull(services);
	}

	//TODO Make tests of creating, editing and deleting, and the rest of functional requirements (Cancel them by an admin)

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
