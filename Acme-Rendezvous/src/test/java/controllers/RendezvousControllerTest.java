
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
		final MockHttpServletRequestBuilder gt;

		gt = MockMvcRequestBuilders.get("/rendezvous/list.do?anonymous=true");

		this.mockMvc.perform(gt).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list")).andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(1))).andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.allOf(Matchers.hasProperty("adultOnly", Matchers.is(false)))));

	}
	/**
	 * Test the pagination of the public list of Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listRendezvousPagePositive() throws Exception {
		final MockHttpServletRequestBuilder gt;

		gt = MockMvcRequestBuilders.get("/rendezvous/list.do?page=1&anonymous=true");

		this.mockMvc.perform(gt).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(4))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.hasValue("rendezvous/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.hasValue(1))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.hasValue(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.hasValue(true)));

	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * The user is major and the rendezvouses with adult only can be listed, then
	 * in the second page there must be the same elements that in the normal list.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listRendezvousOldUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder gt;
		super.authenticate("user1");
		gt = MockMvcRequestBuilders.get("/rendezvous/list.do?anonymous=false");

		this.mockMvc.perform(gt).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.hasValue(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.hasValue(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.hasValue(false)));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * The user is minor and the rendezvouses with adult only must not be listed, then
	 * in the second page there must be 2 items and not 4.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listRendezvousYoungUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder gt;
		super.authenticate("user8");
		gt = MockMvcRequestBuilders.get("/rendezvous/list.do?page=1&anonymous=false");

		this.mockMvc.perform(gt).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(2))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.hasValue(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.hasValue(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.hasValue(false)));

		super.unauthenticate();
	}
}
