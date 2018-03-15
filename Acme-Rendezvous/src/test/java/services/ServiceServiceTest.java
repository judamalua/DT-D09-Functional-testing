
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.ConstraintViolationException;

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
import domain.Category;
import domain.DomainService;
import domain.Manager;
import domain.Request;

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
	@Autowired
	private CategoryService			categoryService;


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

	//TODO Make tests of editing and deleting, and the rest of functional requirements (Cancel them by an admin)

	// Create service tests

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is registered as a manager must be able to Manage his or her
	 * services, which includes listing them, creating them, updating them, and deleting them as long as they are not required by any rendezvouses.
	 * Every test is explained inside
	 * 
	 * @author Juanmi
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateServices() {
		final Collection<Category> emptyCategoryList = new HashSet<Category>();
		final Collection<Category> oneCategoryList = new HashSet<Category>();
		final Collection<Category> twoCategoriesList = new HashSet<Category>();
		final String pictureUrl = "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg";

		oneCategoryList.add(this.categoryService.findOne(super.getEntityId("Category1")));

		twoCategoriesList.add(this.categoryService.findOne(super.getEntityId("Category1")));
		twoCategoriesList.add(this.categoryService.findOne(super.getEntityId("Category2")));

		final Object testingData[][] = {

			{
				// This test checks that authenticated managers can create services with price 0.0
				"Manager1", "Name test", "Description test", pictureUrl, 0.0, twoCategoriesList, null
			}, {
				// This test checks that authenticated managers can create services with all parameters
				"Manager1", "Name test", "Description test", pictureUrl, 15.5, twoCategoriesList, null
			}, {
				// This test checks that authenticated managers can create services with all parameters and just one category
				"Manager1", "Name test", "Description test", pictureUrl, 15.5, oneCategoryList, null
			}, {
				// This test checks that authenticated managers can create services with an empty picture URL
				"Manager1", "Name test", "Description test", "", 15.5, oneCategoryList, null
			}, {
				// This test checks that authenticated managers cannot create services with an empty name
				"Manager1", "", "Description test", "", 15.5, oneCategoryList, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that authenticated managers cannot create services with an empty description
				"Manager1", "Name test", "", "", 15.5, oneCategoryList, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that authenticated managers cannot create services with an empty category list
				"Manager1", "Name test", "Description test", pictureUrl, 15.5, emptyCategoryList, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that users cannot create services
				"User1", "Name test", "Description test", pictureUrl, 15.5, oneCategoryList, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that admins cannot create services
				"Admin1", "Name test", "Description test", pictureUrl, 15.5, oneCategoryList, javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that unauthenticated users cannot create services
				null, "Name test", "Description test", pictureUrl, 15.5, oneCategoryList, IllegalArgumentException.class
			}, {
				// This test checks that authenticated managers cannot create services with negative price
				"Manager1", "Name test", "Description test", pictureUrl, -1.0, twoCategoriesList, ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateServices((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (Collection<Category>) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	// Edit service tests

	/**
	 * This driver checks several tests regarding functional requirement number 5.2: An actor who is registered as a manager must be able to Manage his or her
	 * services, which includes listing them, creating them, updating them, and deleting them as long as they are not required by any rendezvouses.
	 * Every test is explained inside
	 * 
	 * @author Juanmi
	 */
	@Test
	public void driverEditServices() {
		final String pictureUrl = "https://cdns3.eltiempo.es/eltiempo/blog/noticias/2015/07/olas1.jpg";

		final Object testingData[][] = {
			{
				// This test checks that authenticated managers can edit services with all parameters
				"Manager1", "DomainService1", "Name test", "Description test", pictureUrl, 15.5, null
			}, {
				// This test checks that authenticated managers can edit services without picture url
				"Manager1", "DomainService1", "Name test", "Description test", "", 15.5, null
			}, {
				// This test checks that authenticated managers can edit services with price 0.0
				"Manager1", "DomainService1", "Name test", "Description test", "", 0.0, null
			}, {
				// This test checks that authenticated managers cannot edit services without name
				"Manager1", "DomainService1", "", "Description test", "", 15.5, ConstraintViolationException.class
			}, {
				// This test checks that authenticated managers can edit services without description
				"Manager1", "DomainService1", "Name test", "", "", 15.5, ConstraintViolationException.class
			}, {
				// This test checks that authenticated managers can edit services with negative price
				"Manager1", "DomainService1", "Name test", "Description test", "", -1.0, ConstraintViolationException.class
			}, {
				// This test checks that authenticated managers cannot edit services they did not create
				"Manager2", "DomainService1", "Name test", "Description test", "", 15.0, IllegalArgumentException.class
			}, {
				// This test checks that authenticated managers cannot edit services they did not create
				null, "DomainService1", "Name test", "Description test", "", 15.0, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditServices((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Double) testingData[i][5], (Class<?>) testingData[i][6]);
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

	protected void templateCreateServices(final String username, final String name, final String description, final String pictureUrl, final Double price, final Collection<Category> categoriesParameter, final Class<?> expected) {
		Class<?> caught;
		Collection<Request> requests;
		Collection<Category> categories;
		DomainService result;

		caught = null;

		try {
			super.authenticate(username);

			requests = new HashSet<Request>();
			categories = new HashSet<Category>();

			result = this.serviceService.create();

			result.setName(name);
			result.setDescription(description);
			result.setPrice(price);
			result.setPictureUrl(pictureUrl);
			result.setCancelled(false);

			result.setRequests(requests);

			if (categoriesParameter == null)
				result.setCategories(categories);
			else
				result.setCategories(categoriesParameter);

			this.serviceService.save(result);
			this.serviceService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void templateEditServices(final String username, final String servicePopulateName, final String name, final String description, final String pictureUrl, final Double price, final Class<?> expected) {
		Class<?> caught;
		int serviceId;
		DomainService service;

		caught = null;

		try {
			super.authenticate(username);

			serviceId = super.getEntityId(servicePopulateName);

			service = this.serviceService.findOne(serviceId);

			service.setName(name);
			service.setDescription(description);
			service.setPrice(price);
			service.setPictureUrl(pictureUrl);
			service.setCancelled(false);

			this.serviceService.save(service);
			this.serviceService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
