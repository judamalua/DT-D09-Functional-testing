
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

import services.ConfigurationService;
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.user.SimilarUserController;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class SimilarUserControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private SimilarUserController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private RendezvousService		service;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


	//Supporting services -----------------------

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

	/**
	 * Test edit view of rendezvous in the system.
	 * 
	 * Must return 200 code.
	 * The logged user get the view to edit the similars of an Rendezvous.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int rendezvousId = super.getEntityId("Rendezvous8");
		request = MockMvcRequestBuilders.get("/similar/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvous", Matchers.allOf(Matchers.hasProperty("id", Matchers.is(rendezvousId)), Matchers.hasProperty("finalMode", Matchers.is(false)))))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("similar/user/edit.do"))).andExpect(MockMvcResultMatchers.model().attribute("adult", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(7))).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test the edit view of Rendezvouses in the system
	 * Must return 302 code.
	 * The rendezvous is not final and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editNotFinalRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int rendezvousId = super.getEntityId("Rendezvous1");
		request = MockMvcRequestBuilders.get("/similar/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvous", Matchers.allOf(Matchers.hasProperty("id", Matchers.is(rendezvousId)), Matchers.hasProperty("finalMode", Matchers.is(false)))))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("similar/user/edit.do"))).andExpect(MockMvcResultMatchers.model().attribute("adult", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(7))).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test the edit view of Rendezvouses in the system
	 * 
	 * Must return 302 code.
	 * The user is is not the owner of the rendezvouses and must redirect error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editNotCreatedRendezvousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user2");
		final int rendezvousId = super.getEntityId("Rendezvous8");
		request = MockMvcRequestBuilders.get("/similar/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test save correct Similar in the system
	 * Must return 302 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousPositive() throws Exception {
		int userId, rendezvousId, similarId;
		Rendezvous rendezvous, similar;

		super.authenticate("user1");

		userId = super.getEntityId("User1");
		rendezvousId = super.getEntityId("Rendezvous2");
		similarId = super.getEntityId("Rendezvous3");

		rendezvous = this.service.findOne(rendezvousId);
		similar = this.service.findOne(similarId);

		rendezvous.getSimilars().add(similar);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/similar/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("rendezvous", rendezvous).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302))
			.andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5&userId=" + userId));

		super.unauthenticate();
	}
	/**
	 * Test save similar in a Rendezvous in the system
	 * 
	 * Must return 200 code.
	 * There is no one logged and must redirect to error page
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId, similarId;
		Rendezvous rendezvous, similar;

		rendezvousId = super.getEntityId("Rendezvous2");
		similarId = super.getEntityId("Rendezvous3");

		rendezvous = this.service.findOne(rendezvousId);
		similar = this.service.findOne(similarId);

		rendezvous.getSimilars().add(similar);

		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("rendezvous", rendezvous);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}

	/**
	 * Test save adult Rendezvous in the system
	 * 
	 * Must return 200 code.
	 * The user is minor and the rendezvous adult, the must return the adult error code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousNotAdultNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId, similarId;
		Rendezvous rendezvous, similar;

		super.authenticate("user8");

		rendezvousId = super.getEntityId("Rendezvous2");
		similarId = super.getEntityId("Rendezvous3");

		rendezvous = this.service.findOne(rendezvousId);
		similar = this.service.findOne(similarId);

		rendezvous.getSimilars().add(similar);

		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("rendezvous", rendezvous);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.adult.error")));

		super.unauthenticate();
	}

}
