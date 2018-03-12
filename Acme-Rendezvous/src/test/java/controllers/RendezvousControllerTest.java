
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

import services.ConfigurationService;
import services.RendezvousService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class RendezvousControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private RendezvousController	controller;

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
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/rendezvous/list.do?anonymous=true");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(1))).andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasItems(Matchers.allOf(Matchers.hasProperty("adultOnly", Matchers.is(false))))));
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * The user is major and the rendezvouses with adult only can be listed, then the
	 * test check that there is adult and not adult rendezvouses.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listRendezvousOldUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/rendezvous/list.do?anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2))).andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.is(false)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasItems(Matchers.anyOf(Matchers.hasProperty("adultOnly", Matchers.is(false)), Matchers.hasProperty("adultOnly", Matchers.is(true))))));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * The user is minor and the rendezvouses with adult only must not be listed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listRendezvousYoungUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user8");
		request = MockMvcRequestBuilders.get("/rendezvous/list.do?anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(1))).andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.is(false)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasItems(Matchers.allOf(Matchers.hasProperty("adultOnly", Matchers.is(false))))));
		super.unauthenticate();
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 302 code.
	 * The user is minor and the rendezvouses with adult only must not be listed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		request = MockMvcRequestBuilders.get("/rendezvous/list.do?anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * The user is minor and the rendezvouses with adult only must not be listed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void detailedRendezvousNotLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous1");
		request = MockMvcRequestBuilders.get("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=true");

		this.mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("rendezvous/detailed-rendezvous"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/detailed-rendezvous"))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"rendezvous",
					Matchers.allOf(Matchers.hasProperty("id", Matchers.is(rendezvousId)), Matchers.hasProperty("name", Matchers.is("Meeting of friends")), Matchers.hasProperty("moment", Matchers.hasToString("2018-02-24 21:00:00.0")),
						Matchers.hasProperty("adultOnly", Matchers.is(false)), Matchers.hasProperty("deleted", Matchers.is(false)), Matchers.hasProperty("finalMode", Matchers.is(true)))));
	}

	/**
	 * Test detailed Rendezvous view in the system with a user logged. Must return 200 code.
	 * 
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void detailedRendezvousUserLoggedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int rendezvousId;

		super.authenticate("User1");
		rendezvousId = super.getEntityId("Rendezvous1");
		request = MockMvcRequestBuilders.get("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");

		this.mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("rendezvous/detailed-rendezvous"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/detailed-rendezvous"))
			.andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.is(false)))
			.andExpect(MockMvcResultMatchers.model().attribute("userHasCreatedRendezvous", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("userHasRVSPdRendezvous", Matchers.is(true)))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"rendezvous",
					Matchers.allOf(Matchers.hasProperty("id", Matchers.is(rendezvousId)), Matchers.hasProperty("name", Matchers.is("Meeting of friends")), Matchers.hasProperty("moment", Matchers.hasToString("2018-02-24 21:00:00.0")),
						Matchers.hasProperty("adultOnly", Matchers.is(false)), Matchers.hasProperty("deleted", Matchers.is(false)), Matchers.hasProperty("finalMode", Matchers.is(true)))));

		super.unauthenticate();
	}

	/**
	 * Test detailed adult Rendezvous view in the system with a minor user logged. Must return 302 code.
	 * 
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void adultDetailedRendezvousMinorUserLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int rendezvousId;

		super.authenticate("User8");

		rendezvousId = super.getEntityId("Rendezvous2");
		request = MockMvcRequestBuilders.get("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
		super.unauthenticate();
	}

	/**
	 * Test detailed view of Rendezvous in the system. Must return 302 code.
	 * The detailed view must not be display because there is no one logged and is adult.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void adultDetailedRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous2");
		request = MockMvcRequestBuilders.get("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=true");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 302 code.
	 * The detailed view must not be display because the user not is anonymous
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void detailedRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous1");
		request = MockMvcRequestBuilders.get("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
}
