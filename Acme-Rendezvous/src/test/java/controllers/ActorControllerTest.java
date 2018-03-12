
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
	 * Test the display view of an Actor who is the principal, logged as an User. Must return 200 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDisplayActorUserPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int actorId;

		actorId = super.getEntityId("User1");
		super.authenticate("user1"); //The principal

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.hasProperty("id", Matchers.is(actorId))));

		super.unauthenticate();
	}

	/**
	 * Test the display view of an Actor who is the principal, logged as an Admin. Must return 200 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDisplayActorAdminPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int actorId;

		actorId = super.getEntityId("Administrator1");

		super.authenticate("admin"); //The principal

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.hasProperty("id", Matchers.is(actorId))));

		super.unauthenticate();
	}

	/**
	 * Test the display view of an Actor who is the principal, logged as a Manager. Must return 200 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDisplayActorManagerPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int actorId;

		actorId = super.getEntityId("Manager1");

		super.authenticate("manager1"); //The principal

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.hasProperty("id", Matchers.is(actorId))));

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

	//-------------------------- REGISTER USER TESTS ------------------------------
	/**
	 * Test the view to register a new User to the System. Must return 200 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testRegisterUserGETPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;

		super.authenticate(null); //Not logged

		request = MockMvcRequestBuilders.get("/actor/register.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(200)).andExpect(MockMvcResultMatchers.view().name("user/register")).andExpect(MockMvcResultMatchers.forwardedUrl("user/register"))
			.andExpect(MockMvcResultMatchers.model().attribute("actionURL", Matchers.is("actor/register.do"))).andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.hasProperty("id", Matchers.is(0))))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test the view to register a new User to the System, when already logged as an User. Must return 403 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testRegisterUserWhenLoggedAsUserGETNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;

		super.authenticate("user1");

		request = MockMvcRequestBuilders.get("/actor/register.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403"));

		super.unauthenticate();
	}

	/**
	 * Test the view to register a new User to the System, when already logged as a Manager. Must return 403 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testRegisterUserWhenLoggedAsManagerGETNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;

		super.authenticate("manager1");

		request = MockMvcRequestBuilders.get("/actor/register.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403"));

		super.unauthenticate();
	}

	/**
	 * Test the view to register a new User to the System, when already logged as a Manager. Must return 403 code.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testRegisterUserWhenLoggedAsAdminGETNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;

		super.authenticate("admin");

		request = MockMvcRequestBuilders.get("/actor/register.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403"));

		super.unauthenticate();
	}

}
