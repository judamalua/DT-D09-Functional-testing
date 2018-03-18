
package controllers;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.CategoryService;
import services.ConfigurationService;
import services.ServiceService;
import utilities.AbstractTest;
import controllers.manager.ServiceManagerController;
import domain.Category;
import domain.DomainService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ServiceManagerControllerTest extends AbstractTest {

	private MockMvc						mockMvc;

	@InjectMocks
	@Autowired
	private ServiceManagerController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ServiceService				service;

	@Autowired
	private CategoryService				categoryService;

	@Mock
	@Autowired
	private ConfigurationService		configurationService;


	//Supporting services -----------------------

	//Tests--------------------------------------
	@Override
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this.service);
		MockitoAnnotations.initMocks(this.configurationService);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}
	/**
	 * Test the created list of Services of the principal when anyone is logged, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listCreatedServicesNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/service/manager/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Test the created list of Services of the principal when a actor who is not an Manager is logged, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listCreatedServicesNotManagerLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/service/manager/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
		super.unauthenticate();
	}

	/**
	 * Test the created list of Services of the principal in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * Must return 200 code.
	 * The list must contains 5 elements corresponding to the first page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listServiceManagerLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("manager1");
		request = MockMvcRequestBuilders.get("/service/manager/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/list")).andExpect(MockMvcResultMatchers.forwardedUrl("service/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("service/manager/list.do?")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("managedServices", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("managedServices", Matchers.not(Matchers.contains(false))));

		super.unauthenticate();
	}

	/**
	 * Test edit view of Service in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * Must return 200 code.
	 * The logged user get the view of his/her own Service.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editServicePositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("manager1");
		final int serviceId = super.getEntityId("DomainService6");
		request = MockMvcRequestBuilders.get("/service/manager/edit.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("service/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("service", Matchers.hasProperty("id", Matchers.is(serviceId)))).andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasSize(8)))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test the edit view of Services in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 302 code.
	 * There is no one logged and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editServiceNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int serviceId = super.getEntityId("DomainService6");
		request = MockMvcRequestBuilders.get("/service/manager/edit.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}
	/**
	 * Test the edit view of Services in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * Must return 302 code.
	 * The manager is is not the owner of the Service and must redirect error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editNotCreatedServiceNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("manager2");
		final int serviceId = super.getEntityId("DomainService6");
		request = MockMvcRequestBuilders.get("/service/manager/edit.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test the edit view of Services in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 302 code.
	 * The Service has request and the system must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editServiceWithRequestNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("manager1");
		final int serviceId = super.getEntityId("DomainService1");
		request = MockMvcRequestBuilders.get("/service/manager/edit.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Services in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 200 code.
	 * The manager is owner of the Service in final mode and can be displayed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void createServicePositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("manager2");
		request = MockMvcRequestBuilders.get("/service/manager/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("service/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("service", Matchers.hasProperty("id", Matchers.is(0)))).andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasSize(8)))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test create a Services in the system , regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * Must return 302 code.
	 * There is anyone logged and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void createServiceNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		super.unauthenticate();
		request = MockMvcRequestBuilders.get("/service/manager/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Test create a Services in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * Must return 302 code.
	 * There is anyone logged and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void createServiceNotManagerLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("admin1");
		request = MockMvcRequestBuilders.get("/service/manager/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}
	/**
	 * Test save correct Service in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveServicePositive() throws Exception {
		DomainService service;
		int categoryId;
		Category category;
		Collection<Category> categories;
		super.authenticate("manager1");

		categories = new HashSet<>();
		categoryId = super.getEntityId("Category1");
		category = this.categoryService.findOne(categoryId);
		categories.add(category);

		service = new DomainService();
		service.setCategories(categories);

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/service/manager/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Service").param("description", "New Description").param("moment", "09/04/2019 00:00").param("pictureUrl", "")
					.param("cancelled", "false").param("price", "220").flashAttr("service", service).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:list.do"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test save Service in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 200 code.
	 * There is no one logged and must redirect to error page
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveServiceNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		DomainService service;
		int categoryId;
		Category category;
		Collection<Category> categories;
		super.authenticate("admin1");

		categories = new HashSet<>();
		categoryId = super.getEntityId("Category1");
		category = this.categoryService.findOne(categoryId);
		categories.add(category);

		service = new DomainService();
		service.setCategories(categories);

		request = MockMvcRequestBuilders.post("/service/manager/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Service").param("description", "New Description").param("moment", "09/04/2019 00:00").param("pictureUrl", "")
			.param("cancelled", "false").param("price", "220").flashAttr("service", service).param("save", "");
		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("service/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("service.commit.error")));

		super.unauthenticate();
	}
	/**
	 * Test save Service with past moment in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 200 code.
	 * There is no manager logged and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveServiceNotManagerLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		DomainService service;
		int categoryId;
		Category category;
		Collection<Category> categories;
		super.authenticate("admin1");

		categories = new HashSet<>();
		categoryId = super.getEntityId("Category1");
		category = this.categoryService.findOne(categoryId);
		categories.add(category);

		service = new DomainService();
		service.setCategories(categories);

		request = MockMvcRequestBuilders.post("/service/manager/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Service").param("description", "New Description").param("moment", "09/04/2019 00:00").param("pictureUrl", "")
			.param("cancelled", "false").param("price", "220").flashAttr("service", service).param("save", "");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/edit")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.forwardedUrl("service/edit")).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("service.commit.error")));

		super.unauthenticate();
	}

	/**
	 * Test save blank parameters Service in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 200 code.
	 * The Service has blank required parameters then they must be redirect to the edit page with these fields with errors.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveServiceBlankNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("manager2");

		request = MockMvcRequestBuilders.post("/service/manager/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "").param("description", "").param("pictureUrl", "").param("cancelled", "false").param("price", "")
			.sessionAttr("service", new DomainService()).param("save", "");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("service/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("service.params.error"))).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("service", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("service", "description")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("service", "price"));

		super.unauthenticate();
	}

	/**
	 * Test save existing Service in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	//@Test
	public void saveExistingServicePositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		DomainService service;
		int serviceId;

		super.authenticate("manager1");
		serviceId = super.getEntityId("DomainService6");
		service = this.service.findOne(serviceId);

		request = MockMvcRequestBuilders.post("/service/manager/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "changed").param("description", "changed").param("cancelled", "false").param("price", "200")
			.flashAttr("service", service).param("save", "");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test delete Service in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteServicePositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int serviceId;
		final DomainService service;

		super.authenticate("manager1");

		serviceId = super.getEntityId("DomainService6");
		service = this.service.findOne(serviceId);
		request = MockMvcRequestBuilders.post("/service/manager/edit.do").param("delete", "").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("service", service);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5"));

		super.unauthenticate();
	}
	/**
	 * Test delete Service in the system, regarding functional requirement 5.2
	 * An actor who is registered as a manager must be able to Manage his or her services, which includes listing them, creating them, updating
	 * them, and deleting them as long as they are not required by any rendezvouses.
	 * 
	 * 
	 * Must return 200 code.
	 * The service has request and the system must return error code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteRequestedServiceNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int serviceId;
		final DomainService service;

		super.authenticate("manager1");

		serviceId = super.getEntityId("DomainService1");
		service = this.service.findOne(serviceId);
		request = MockMvcRequestBuilders.post("/service/manager/edit.do").param("delete", "").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("service", service);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("service/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("service.commit.error")));

		super.unauthenticate();
	}

}
