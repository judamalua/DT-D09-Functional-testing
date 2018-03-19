
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
import utilities.AbstractTest;
import controllers.admin.ActorAdminController;
import domain.Administrator;
import domain.User;
import forms.UserAdminForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ActorAdminControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ActorAdminController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ActorService			actorservice;

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
		MockitoAnnotations.initMocks(this.adminservice);
		MockitoAnnotations.initMocks(this.adminservice);
		MockitoAnnotations.initMocks(this.configurationService);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}

	//Positives***************************************************************************************
	/**
	 * An administrator can be registered in the system
	 * 
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCanRegisterAsAdmin() throws Exception {
		super.authenticate("Admin1");

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/actor/admin/register.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Gutierrez Lopez").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
					.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").param("userAccount.username", "fernanguti").param("userAccount.password", "fernanguti").param("confirmPassword", "fernanguti")
					.sessionAttr("user", new User()).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/welcome/index.do"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/welcome/index.do?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * An actor who is authenticated as an administrator must be able to: Edit and Update his profile
	 * 
	 * Administrator Edit
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void AdminCanEditHisProfile() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("Admin1");
		final Administrator admin = (Administrator) this.actorservice.findActorByPrincipal();

		request = MockMvcRequestBuilders.get("/actor/admin/edit.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(admin.getName())), Matchers.hasProperty("birthDate", Matchers.is(admin.getBirthDate())))));

		super.unauthenticate();
	}
	/**
	 * 
	 * 
	 * Administrators can edit his profiles
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testAdminsCanEditHisProfile() throws Exception {
		UserAdminForm form;
		super.authenticate("Admin1");
		final Administrator admin = (Administrator) this.actorservice.findActorByPrincipal();
		form = this.actorservice.deconstruct(admin);

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/actor/admin/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Gutiérrez López").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
					.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").flashAttr("actor", form).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/actor/display.do")).andExpect(MockMvcResultMatchers.redirectedUrl("/actor/display.do?pagesize=5"));

		super.unauthenticate();
	}

	//Negatives***************************************************************************************
	/**
	 * An actor who is authenticated as an administrator must be able to: Edit and Update his profile
	 * 
	 * Users can't edit an administrator profile
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void UsersCanEditAdminProfile() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("User1");

		request = MockMvcRequestBuilders.get("/actor/admin/edit.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("misc/panic")).andExpect(MockMvcResultMatchers.forwardedUrl("misc/panic"));

		super.unauthenticate();
	}

	/**
	 * An actor who is authenticated as an administrator must be able to: Edit and Update his profile
	 * 
	 * Managers can't edit an administrator profile
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void ManagersCanEditAdminProfile() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("Manager1");

		request = MockMvcRequestBuilders.get("/actor/admin/edit.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("misc/panic")).andExpect(MockMvcResultMatchers.forwardedUrl("misc/panic"));

		super.unauthenticate();
	}

	/**
	 * Administrators can edit his profiles
	 * 
	 * Parameters errors
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void AdminsCantEditHisProfileWithErrors() throws Exception {
		UserAdminForm form;
		super.authenticate("Admin1");
		final Administrator admin = (Administrator) this.actorservice.findActorByPrincipal();
		form = this.actorservice.deconstruct(admin);

		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/actor/admin/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "").param("surname", "Gutiérrez López").param("birthDate", "09/04/2000").param("email", "ferguti90")
				.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").flashAttr("actor", form).param("save", "")).andExpect(MockMvcResultMatchers.status().isOk());

		super.unauthenticate();
	}
	/**
	 * An administrator can´t be registered in the system with errors
	 * 
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCantRegisterAsAdminWithErrors() throws Exception {
		super.authenticate("Admin1");

		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/actor/admin/register.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "").param("surname", "").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
				.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").param("userAccount.username", "fernanguti").param("userAccount.password", "fernanguti").param("confirmPassword", "").sessionAttr("admin", new Administrator())
				.param("save", "")).andExpect(MockMvcResultMatchers.status().isOk());

	}

}
