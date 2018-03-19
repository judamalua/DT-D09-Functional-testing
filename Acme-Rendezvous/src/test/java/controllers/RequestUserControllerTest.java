
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
import org.springframework.http.MediaType;
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
import services.RequestService;
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.user.RequestUserController;
import domain.Request;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class RequestUserControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private RequestUserController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private RequestService			service;

	@Autowired
	private RendezvousService		rendezvousService;

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
	 * Test list of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	//@Test
	public void listRequestNotLoggedNegative() throws Exception {
		MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous1");

		request = MockMvcRequestBuilders.get("/request/user/list.do?rendezvousId" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}
	
	//@Test
		public void listAllRequestNotLoggedNegative() throws Exception {
			MockHttpServletRequestBuilder request;


			request = MockMvcRequestBuilders.get("/request/user/list.do");

			this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403"))
				.andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
		}

	/**
	 * Test list of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	//@Test
	public void listNotOwnRequestNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;
		super.authenticate("user5");
		rendezvousId = super.getEntityId("Rendezvous1");

		request = MockMvcRequestBuilders.get("/request/user/list.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test list of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * The list must contains 5 elements corresponding to the first page.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void listRequestsUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;
		super.authenticate("user1");

		rendezvousId = super.getEntityId("Rendezvous4");

		request = MockMvcRequestBuilders.get("/request/user/list.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/list")).andExpect(MockMvcResultMatchers.forwardedUrl("request/list"))
		.andExpect(MockMvcResultMatchers.model().attribute("requests", Matchers.hasSize(2)));

		super.unauthenticate();
	}
	
	@Test
	public void listAllRequestsUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");


		request = MockMvcRequestBuilders.get("/request/user/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/list")).andExpect(MockMvcResultMatchers.forwardedUrl("request/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("requests", Matchers.hasSize(2)));

		super.unauthenticate();
	}

	/**
	 * Test create of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * Must return 200 code.
	 * The user is owner of the request in final mode and can be displayed.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void createRequestPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int serviceId;

		serviceId = super.getEntityId("DomainService1");

		super.authenticate("user1");

		request = MockMvcRequestBuilders.get("/request/user/create.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/edit"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("request/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("request", Matchers.allOf(Matchers.hasProperty("id", Matchers.is(0)))))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test create of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * 
	 * Must return 302 code.
	 * There is anyone logged and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void createRequestNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous2");
		request = MockMvcRequestBuilders.get("/request/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5&rendezvousId=" + rendezvousId));
	}

	/**
	 * Test save of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * Must return 302 code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	//@Test
	public void saveRequestPositive() throws Exception {
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous4");
		super.authenticate("user1");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/request/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("rendezvous", "" + rendezvousId).param("comment", "New request").param("moment", "01/01/1990 00:00").param("service", "" +super.getEntityId("DomainService1"))
			.param("creditCard", "" + super.getEntityId("CreditCard1")).sessionAttr("request", new Request()).param("save", ""))
			.andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:list.do?rendezvousId=" + rendezvousId))
			.andExpect(MockMvcResultMatchers.redirectedUrl("list.do?rendezvousId=" + rendezvousId + "&pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test save of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * 
	 * Must return 200 code.
	 * There is no one logged and must redirect to error page
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void saveRequestNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");

		request = MockMvcRequestBuilders.post("/request/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("rendezvous", "" + rendezvousId).param("comment", "New request").param("moment", "01/01/1990 00:00").param("service", "" +super.getEntityId("DomainService1"))
				.param("creditCard", "" + super.getEntityId("CreditCard1")).sessionAttr("request", new Request()).param("save", "");
		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}

	/**
	 * Test save of Requests of an request, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the requests that are associated
	 * with a request that he or she’s created previously.
	 * 
	 * Must return 200 code.
	 * The moment must be in future, then the system must return the past error code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void saveRequestNegative() throws Exception {
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous4");
		super.authenticate("user1");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/request/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("rendezvous", "" + rendezvousId).param("comment", "New request").param("moment", "01/01/1990 00:00").param("service", "" +super.getEntityId("DomainService1"))
			.param("creditCard", "" ).sessionAttr("request", new Request()).param("save", ""))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/edit")).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("request.params.error")));

		super.unauthenticate();
	}

	
}
