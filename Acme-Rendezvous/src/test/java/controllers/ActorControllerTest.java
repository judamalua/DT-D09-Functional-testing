
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.ActorService;
import services.ConfigurationService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ActorControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	//Controller under test ----------------------
	@InjectMocks
	@Autowired
	private ActorController			controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ActorService			service;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


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

	//-------------------------- DISPLAY ACTOR TESTS ------------------------------

	/**
	 * Test the display view of an Actor who is the principal. Must return 200 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDisplayActorPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;

		super.authenticate("user1"); //The principal

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.hasProperty("id", Matchers.not(0))));

		super.unauthenticate();
	}

	/**
	 * Test the display view of an Actor who is the principal, but nobody is logged. Must return 302 code and redirect to the misc/403 view.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDisplayActorNotLoggedNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;

		super.authenticate(null); //Not logged

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403"));

		super.unauthenticate();
	}

}
