
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
import domain.Administrator;

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
	 * Test that no logged can display the profile of a user
	 * 
	 * @throws Exception
	 * @author Luis
	 */
	@Test
	public void listRendezvousYoungUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("Admin1");
		final Administrator admin = (Administrator) this.actorservice.findActorByPrincipal();

		request = MockMvcRequestBuilders.get("/actor/display.do?actorId=" + admin.getId());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("actor/display")).andExpect(MockMvcResultMatchers.forwardedUrl("actor/display"))
			.andExpect(MockMvcResultMatchers.model().attribute("actor", Matchers.allOf(Matchers.hasProperty("name", Matchers.is(admin.getName())), Matchers.hasProperty("birthDate", Matchers.is(admin.getBirthDate())))));
	}
}
