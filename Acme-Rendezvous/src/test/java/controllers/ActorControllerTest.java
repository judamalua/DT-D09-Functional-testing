
package controllers;

import java.util.Date;

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
	 * Test that only administrator can display his profile
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
	 * Test no logged can´t display his profile
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
	 * Test users can´t display administrators profile
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
	 * Test managers can´t display administrators profile
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
	 * Test not authenticated can register as user
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCanRegisterAsUser() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate(null);
		final User newUser;
		final Date birthDate = new Date();
		newUser = this.userservice.create();
		newUser.setName("Fernando");
		newUser.setSurname("Gutiérrez López");
		newUser.setBirthDate(birthDate);
		newUser.setEmail("ferguti90@gmail.com");
		newUser.setPhoneNumber("606587789");
		newUser.setPostalAddress("Calle Picadero 9");
		newUser.getUserAccount().setUsername("fernanguti");
		newUser.getUserAccount().setPassword("ferguti");

		request = MockMvcRequestBuilders.get("/actor/register.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(newUser.getName())), Matchers.hasProperty("birthDate", Matchers.is(newUser.getBirthDate())))));

		super.unauthenticate();
	}

}
