
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
import controllers.user.ActorUserController;
import domain.User;
import forms.UserAdminForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ActorUserControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ActorUserController		controller;

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
	 * An actor who is authenticated must be able to: Edit and Update his profile
	 * 
	 * List the users in the system
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void UserCanEditHisProfile() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("User1");
		final User user = (User) this.actorservice.findActorByPrincipal();

		request = MockMvcRequestBuilders.get("/actor/user/edit.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(user.getName())), Matchers.hasProperty("birthDate", Matchers.is(user.getBirthDate())))));

		super.unauthenticate();
	}
	/**
	 * An actor who is authenticated must be able to: Edit and Update his profile
	 * 
	 * Users Can Edit His Profiles
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testUsersCanEditHisProfile() throws Exception {
		UserAdminForm form;
		super.authenticate("User1");
		final User user = (User) this.actorservice.findActorByPrincipal();
		form = this.actorservice.deconstruct(user);

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/actor/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Gutiérrez López").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
					.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").flashAttr("actor", form).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/user/display.do?anonymous=false")).andExpect(MockMvcResultMatchers.redirectedUrl("/user/display.do?anonymous=false&pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * An actor who is authenticated must be able to: Edit and Update his profile
	 * 
	 * Users can´t update his profiles with errors
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testUsersCantUpdateHisProfilesWithErrors() throws Exception {
		UserAdminForm form;
		super.authenticate("User1");
		final User user = (User) this.actorservice.findActorByPrincipal();
		form = this.actorservice.deconstruct(user);

		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/actor/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Gutiérrez López").param("birthDate", "09/04/3000").param("email", "ferguti90")
				.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").flashAttr("actor", form).param("save", "")).andExpect(MockMvcResultMatchers.status().isOk());

		super.unauthenticate();
	}
}
