
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
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.admin.RendezvousAdminController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class RendezvousAdminControllerTest extends AbstractTest {

	private MockMvc						mockMvc;

	@InjectMocks
	@Autowired
	private RendezvousAdminController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private RendezvousService			service;

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
	 * Test delete a Rendezvous when anyone is logged.
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");

		request = MockMvcRequestBuilders.get("/rendezvous/admin/delete.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403"));
	}

	/**
	 * Test delete a Rendezvous when an actor who is not an Administrator is logged.
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteRendezvousNotAdminLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");
		super.authenticate("user1");

		request = MockMvcRequestBuilders.get("/rendezvous/admin/delete.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403"));

		super.unauthenticate();
	}

	/**
	 * Test delete a Rendezvous when an Administrator is logged.
	 * Must return 302 code and redirect to list Rendezvouses page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteRendezvousAdminLoggedPositve() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");
		super.authenticate("admin1");

		request = MockMvcRequestBuilders.get("/rendezvous/admin/delete.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/list.do?anonymous=false"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/rendezvous/list.do?anonymous=false"));

		super.unauthenticate();
	}

	/**
	 * Test delete a not existing Rendezvous when an Administrator is logged.
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteNotExistingRendezvousAdminLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		super.authenticate("admin1");

		request = MockMvcRequestBuilders.get("/rendezvous/admin/delete.do?rendezvousId=" + 1);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403"));

		super.unauthenticate();
	}
}
