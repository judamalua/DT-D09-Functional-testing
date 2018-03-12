
package controllers;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.ConfigurationService;
import services.ServiceService;
import utilities.AbstractTest;
import domain.DomainService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ServiceControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ServiceController		controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ServiceService			serviceService;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


	//Supporting services -----------------------

	//Tests--------------------------------------
	@Override
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this.serviceService);
		MockitoAnnotations.initMocks(this.configurationService);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}
	/**
	 * Test the public list of Services in the system, regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the services that are available in the system.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@Test
	public void listServicesNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/service/list.do?anonymous=true");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}

	/**
	 * Test the public list of Services in the system, regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the services that are available in the system.
	 * 
	 * Must return 200 code.
	 * The anonymous is false and there is no one logged, the system must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@Test
	public void listServicesAnonymousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/service/list.do?anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}

	/**
	 * Test the public list of Services in the system, regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the services that are available in the system.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listServicesUserLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		Page<DomainService> services;
		Pageable pageable;

		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/service/list.do?anonymous=false");
		pageable = new PageRequest(0, this.configurationService.findConfiguration().getPageSize());

		services = this.serviceService.findNotCancelledServices(pageable);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/list")).andExpect(MockMvcResultMatchers.forwardedUrl("service/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("service/list.do?anonymous=false&")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasItems(Matchers.isIn(services.getContent()), Matchers.allOf(Matchers.hasProperty("cancelled", Matchers.is(false))))))
			.andExpect(MockMvcResultMatchers.model().attribute("managedServices", Matchers.hasSize(0)));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Services in the system,regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the services that are available in the system.
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listServicesManagerLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		Page<DomainService> services;
		Pageable pageable;

		super.authenticate("manager1");
		request = MockMvcRequestBuilders.get("/service/list.do?anonymous=false");
		pageable = new PageRequest(0, this.configurationService.findConfiguration().getPageSize());

		services = this.serviceService.findNotCancelledServices(pageable);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/list")).andExpect(MockMvcResultMatchers.forwardedUrl("service/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("service/list.do?anonymous=false&")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasItems(Matchers.isIn(services.getContent()), Matchers.allOf(Matchers.hasProperty("cancelled", Matchers.is(false))))))
			.andExpect(MockMvcResultMatchers.model().attribute("managedServices", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("managedServices", Matchers.not(Matchers.contains(false))));

		super.unauthenticate();
	}
	/**
	 * Test the public list of Services in the system with pagination,regarding functional requirement 4.2: An actor who
	 * is authenticated as a user must be able to list the services that are available in the system.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listServicesPageLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		Page<DomainService> services;
		Pageable pageable;

		super.authenticate("manager1");
		request = MockMvcRequestBuilders.get("/service/list.do?anonymous=false&page=1");
		pageable = new PageRequest(1, this.configurationService.findConfiguration().getPageSize());

		services = this.serviceService.findNotCancelledServices(pageable);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/list")).andExpect(MockMvcResultMatchers.forwardedUrl("service/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasSize(1))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("service/list.do?anonymous=false&")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(1))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("services", Matchers.hasItems(Matchers.isIn(services.getContent()), Matchers.allOf(Matchers.hasProperty("cancelled", Matchers.is(false))))))
			.andExpect(MockMvcResultMatchers.model().attribute("managedServices", Matchers.hasSize(1))).andExpect(MockMvcResultMatchers.model().attribute("managedServices", Matchers.not(Matchers.contains(false))));

		super.unauthenticate();
	}
}
