
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
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class ManagerControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private ManagerController		controller;

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
	 * 3.1 An actor who is not authenticated must be able to:Register to the system as a manager
	 * 
	 * Manager can Register in the system
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void ManagerCanRegister() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate(null);

		request = MockMvcRequestBuilders.get("/actor/register-manager.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("manager/register")).andExpect(MockMvcResultMatchers.forwardedUrl("manager/register"));

		super.unauthenticate();
	}
	/**
	 * 3.1 An actor who is not authenticated must be able to:Register to the system as a manager
	 * 
	 * 
	 * Test not authenticated can register as manager
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCanRegisterAsManeger() throws Exception {
		super.authenticate(null);

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/actor/register-manager.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Luis").param("surname", "Gutiérrez López").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
					.param("phoneNumber", "606587789").param("vat", "ES12E223EE").param("postalAddress", "Calle Picadero 9").param("userAccount.username", "luisguti").param("userAccount.password", "luisguti").param("confirmPassword", "luisguti")
					.sessionAttr("manager", new Manager()).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/welcome/index.do"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/welcome/index.do?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * 3.1 An actor who is not authenticated must be able to:Register to the system as a manager
	 * 
	 * 
	 * Test not authenticated can´t register as manager with errors
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void testNoAuthenticatedCantRegisterAsManegerWithErrors() throws Exception {
		super.authenticate(null);

		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/actor/register-manager.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Luis").param("surname", "Gutiérrez López").param("birthDate", "09/04/2000").param("email", "ferguti90@gmail.com")
				.param("phoneNumber", "606587789").param("vat", "3EE").param("postalAddress", "Calle Picadero 9").param("userAccount.username", "luisguti").param("userAccount.password", "guti").param("confirmPassword", "luisguti")
				.sessionAttr("manager", new Manager()).param("save", "")).andExpect(MockMvcResultMatchers.status().isOk());

		super.unauthenticate();
	}

}
