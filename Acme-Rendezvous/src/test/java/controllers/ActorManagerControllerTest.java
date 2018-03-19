
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
import services.ManagerService;
import utilities.AbstractTest;
import controllers.manager.ActorManagerController;
import domain.Manager;
import forms.ManagerForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ActorManagerControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ActorManagerController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private ActorService			actorservice;

	@Mock
	@Autowired
	private ManagerService			managerservice;

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
		MockitoAnnotations.initMocks(this.managerservice);
		MockitoAnnotations.initMocks(this.adminservice);
		MockitoAnnotations.initMocks(this.configurationService);
		MockitoAnnotations.initMocks(this.controller);

		Mockito.validateMockitoUsage();
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
	}

	/**
	 * An actor who is authenticated as a manager must be able to: Edit and Update his profile
	 * 
	 * Edit the profile of a manager
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void ManagerCanEditHisProfile() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("Manager1");
		final Manager manager = (Manager) this.actorservice.findActorByPrincipal();

		request = MockMvcRequestBuilders.get("/actor/manager/edit.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(manager.getName())), Matchers.hasProperty("birthDate", Matchers.is(manager.getBirthDate())))));

		super.unauthenticate();
	}
	/**
	 * 
	 * 
	 * Managers Can Edit His Profiles
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testManagerCanEditHisProfile() throws Exception {
		ManagerForm form;
		super.authenticate("Manager1");
		final Manager manager = (Manager) this.actorservice.findActorByPrincipal();
		form = this.managerservice.deconstruct(manager);

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/actor/manager/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Fernando").param("surname", "Gutiérrez López").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
					.param("phoneNumber", "606587789").param("postalAddress", "Calle Picadero 9").param("vat", "ES14147878").flashAttr("actor", form).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/actor/display.do")).andExpect(MockMvcResultMatchers.redirectedUrl("/actor/display.do?pagesize=5"));

		super.unauthenticate();
	}
}
