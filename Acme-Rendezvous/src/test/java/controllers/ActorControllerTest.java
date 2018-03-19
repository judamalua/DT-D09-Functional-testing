
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
import org.springframework.http.MediaType;
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
import domain.Administrator;
import domain.Manager;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ActorControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ActorController			controller;

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
	 * 
	 * 
	 * Test administrator can display his profile
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void AdministratorCanDisplayHisProfile() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("Admin1");
		final Administrator admin = (Administrator) this.actorservice.findActorByPrincipal();

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(admin.getName())), Matchers.hasProperty("birthDate", Matchers.is(admin.getBirthDate())))));

		super.unauthenticate();
	}

	/**
	 * 
	 * 
	 * Test no logged can't display his profile
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoLoggedCantDisplayhisProfile() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate(null);

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * 5.1 An actor who is authenticated as a user must be able to:Do the same as an actor who is not authenticated, but register to the system.
	 * 
	 * Test users can display theirs profile
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testUsersCantDisplayActorsProfiles() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("User1");
		final User user = (User) this.actorservice.findActorByPrincipal();

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(user.getName())), Matchers.hasProperty("birthDate", Matchers.is(user.getBirthDate())))));

		super.unauthenticate();
	}

	/**
	 * 
	 * 
	 * Test managers can display theirs profiles
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testManagersCantDisplayActorsProfiles() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("Manager1");
		final Manager manager = (Manager) this.actorservice.findActorByPrincipal();

		request = MockMvcRequestBuilders.get("/actor/display.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(manager.getName())), Matchers.hasProperty("birthDate", Matchers.is(manager.getBirthDate())))));

		super.unauthenticate();
	}

	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * 
	 * Test not authenticated can register as user
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCanRegisterAsUser() throws Exception {
		super.authenticate(null);

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/actor/register.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Guti�rrez L�pez").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
					.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").param("userAccount.username", "fernanguti").param("userAccount.password", "fernanguti").param("confirmPassword", "fernanguti")
					.sessionAttr("user", new User()).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/welcome/index.do"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/welcome/index.do?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a user.
	 * 
	 * 
	 * Test not authenticated can't register as user with fails
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCanRegisterAsUserWithFails() throws Exception {
		super.authenticate(null);

		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/actor/register.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Guti�rrez L�pez").param("birthDate", "09/04/2020").param("email", "ferguti90@gmail.com")
				.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").param("userAccount.username", "fernanguti").param("userAccount.password", "fernanguti").param("confirmPassword", "fernanguti").sessionAttr("user", new User())
				.param("save", "")).andExpect(MockMvcResultMatchers.status().is(200));

		super.unauthenticate();
	}

	/**
	 * Test that method only register as an user
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testOnlyResgisterAsAnUser() throws Exception {
		super.authenticate(null);

		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/actor/register.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Guti�rrez L�pez").param("birthDate", "09/04/2020").param("email", "ferguti90@gmail.com")
				.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").param("userAccount.username", "fernanguti").param("userAccount.password", "fernanguti").param("confirmPassword", "fernanguti").sessionAttr("user", new Manager())
				.param("save", "")).andExpect(MockMvcResultMatchers.status().is(200));

		super.unauthenticate();
	}

}
