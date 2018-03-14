
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
import services.AdministratorService;
import services.ConfigurationService;
import services.UserService;
import utilities.AbstractTest;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class UserControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private UserController			controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ActorService			actorservice;

	@Mock
	@Autowired
	private UserService				userservice;

	@Mock
	@Autowired
	private AdministratorService	adminservice;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


	//Supporting services -----------------------

	//Tests--------------------------------------
	@Override
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this.actorservice);
		MockitoAnnotations.initMocks(this.userservice);
		MockitoAnnotations.initMocks(this.adminservice);
		MockitoAnnotations.initMocks(this.configurationService);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}

	/**
	 * 4.2 An actor who is not authenticated must be able to: List the users of the system and navigate to their profiles, which include personal data
	 * and the list of rendezvouses that they've attended or are going to attend
	 * 
	 * No Authenticated can list the users in the system
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void NoAuthenticatedListUsersOfSystem() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate(null);

		request = MockMvcRequestBuilders.get("/user/list.do?anonymous=true");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0)));

		super.unauthenticate();
	}

	/**
	 * 5.1 An actor who is authenticated as a user must be able to:Do the same as an actor who is not authenticated, but register to the system.
	 * 
	 * Users can list the users in the system
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void UsersListUsersOfSystem() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("User1");

		request = MockMvcRequestBuilders.get("/user/list.do?anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0)));

		super.unauthenticate();
	}
	/**
	 * 4.2 An actor who is not authenticated must be able to: List the users of the system and navigate to their profiles, which include personal data
	 * and the list of rendezvouses that they've attended or are going to attend
	 * 
	 * Navigate to the profiles of the users
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCanDisplayUsersProfiles() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate(null);
		final int userId = this.getEntityId("User1");
		final User user = (User) this.actorservice.findOne(userId);

		request = MockMvcRequestBuilders.get("/user/display.do?actorId=" + userId + "&anonymous=true");

		this.mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("actor/display"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"actor",
					Matchers.allOf(Matchers.hasProperty("name", Matchers.is(user.getName())), Matchers.hasProperty("birthDate", Matchers.is(user.getBirthDate())), Matchers.hasProperty("rsvpRendezvouses", Matchers.is(user.getRsvpRendezvouses())),
						Matchers.hasProperty("createdRendezvouses", Matchers.is(user.getCreatedRendezvouses())))));

		super.unauthenticate();
	}

	/**
	 * 5.1 An actor who is authenticated as a user must be able to:Do the same as an actor who is not authenticated, but register to the system.
	 * 
	 * Navigate to the profiles of the users
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testUsersCantDisplayUsersProfiles() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("User2");
		final int userId = this.getEntityId("User1");
		final User user = (User) this.actorservice.findOne(userId);

		request = MockMvcRequestBuilders.get("/user/display.do?actorId=" + userId + "&anonymous=true");

		this.mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("actor/display"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"actor",
					Matchers.allOf(Matchers.hasProperty("name", Matchers.is(user.getName())), Matchers.hasProperty("birthDate", Matchers.is(user.getBirthDate())), Matchers.hasProperty("rsvpRendezvouses", Matchers.is(user.getRsvpRendezvouses())),
						Matchers.hasProperty("createdRendezvouses", Matchers.is(user.getCreatedRendezvouses())))));

		super.unauthenticate();
	}
}
