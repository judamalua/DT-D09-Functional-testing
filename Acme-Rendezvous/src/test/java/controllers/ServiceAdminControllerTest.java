
package controllers;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
import controllers.admin.ServiceAdminController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ServiceAdminControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ServiceAdminController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ServiceService			service;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


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
	 * Test cancel a Service when anyone is logged regarding functional requirement An actor who is authenticated as an administrator must be able to cancel a service that he or she finds
	 * inappropriate. Such services cannot be requested for any rendezvous.They must be flagged appropriately when listed..
	 * 
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void cancelServiceNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int serviceId;

		serviceId = super.getEntityId("DomainService6");

		request = MockMvcRequestBuilders.get("/service/admin/cancel.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Test cancel a Service when an actor who is not an Administrator is logged.
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void cancelServiceNotAdminLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int serviceId;

		serviceId = super.getEntityId("DomainService6");
		super.authenticate("user1");

		request = MockMvcRequestBuilders.get("/service/admin/cancel.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test cancel a Service when an Administrator is logged, regardinf functional requirement 6.1
	 * An actor who is authenticated as an administrator must be able to cancel a service that he or she finds
	 * inappropriate.Such services cannot be requested for any rendezvous.They must be flagged appropriately when listed..
	 * 
	 * Must return 302 code and redirect to list Rendezvouses page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void cancelServiceAdminLoggedPositve() throws Exception {
		final MockHttpServletRequestBuilder request;
		int serviceId;

		serviceId = super.getEntityId("DomainService6");
		super.authenticate("admin1");

		request = MockMvcRequestBuilders.get("/service/admin/cancel.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/service/list.do?anonymous=false"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/service/list.do?anonymous=false&pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test cancel a not existing Service when an Administrator is logged, regarding functional requirement 6.1 An actor who is authenticated as an administrator must be able to cancel a service that he or she finds
	 * inappropriate. Such services cannot be requested for any rendezvous.They must be flagged appropriately when listed..
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void cancelNotExistingServiceAdminLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		super.authenticate("admin1");

		request = MockMvcRequestBuilders.get("/service/admin/cancel.do?serviceId=" + 1);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}
}
