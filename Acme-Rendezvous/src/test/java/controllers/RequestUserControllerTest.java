
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.ConfigurationService;
import services.RequestService;
import services.ServiceService;
import utilities.AbstractTest;
import controllers.user.RequestUserController;
import domain.DomainService;
import domain.Request;

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
	private ServiceService			serviceService;

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
	 * Test list of Requests for user.
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
	 * Test list of Requests for user.
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
	 * Requirement 4.3 An actor who is authenticated as a user must be able to:
	 * Request a service for one of the rendezvouses that he or she's created. He or she must specify a valid credit card in every request for a service. Optionally, he or she can provide some comments in the request.
	 * 
	 * Must return 200 code.
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

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("request/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("request", Matchers.allOf(Matchers.hasProperty("id", Matchers.is(0))))).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Requirement 4.3 An actor who is authenticated as a user must be able to:
	 * Request a service for one of the rendezvouses that he or she's created. He or she must specify a valid credit card in every request for a service. Optionally, he or she can provide some comments in the request.
	 * 
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
		int serviceId;

		serviceId = super.getEntityId("DomainService4");
		request = MockMvcRequestBuilders.get("/request/user/create.do?serviceId=" + serviceId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Requirement 4.3 An actor who is authenticated as a user must be able to:
	 * Request a service for one of the rendezvouses that he or she's created. He or she must specify a valid credit card in every request for a service. Optionally, he or she can provide some comments in the request.
	 * 
	 * Must return 302 code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void saveRequestPositive() throws Exception {
		int rendezvousId;
		Request request;

		final Integer idService = super.getEntityId("DomainService3");
		final DomainService dmService = this.serviceService.findOne(idService);
		rendezvousId = super.getEntityId("Rendezvous4");
		super.authenticate("user1");
		request = new Request();
		request.setService(dmService);
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/request/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("rendezvousId", "" + rendezvousId).param("comment", "New request").param("moment", "01/01/1990 00:00")
					.param("creditCard.holderName", "Test1").param("creditCard.brandName", "Test1").param("creditCard.number", "4929175934737503").param("creditCard.expirationMonth", "10").param("creditCard.expirationYear", "90")
					.param("creditCard.cvv", "333").param("creditCard.cookieToken", "TEST").flashAttr("request", request).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false&pagesize=5"));

		super.unauthenticate();
	}
	/**
	 * Requirement 4.3 An actor who is authenticated as a user must be able to:
	 * Request a service for one of the rendezvouses that he or she's created. He or she must specify a valid credit card in every request for a service. Optionally, he or she can provide some comments in the request.
	 * 
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
		Request domainRequest;

		final Integer idService = super.getEntityId("DomainService3");
		final DomainService dmService = this.serviceService.findOne(idService);
		domainRequest = new Request();
		domainRequest.setService(dmService);
		rendezvousId = super.getEntityId("Rendezvous4");

		request = MockMvcRequestBuilders.post("/request/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("rendezvousId", "" + rendezvousId).param("comment", "New request").param("moment", "01/01/1990 00:00")
			.param("creditCard.holderName", "Test1").param("creditCard.brandName", "Test1").param("creditCard.number", "4929175934737503").param("creditCard.expirationMonth", "10").param("creditCard.expirationYear", "90").param("creditCard.cvv", "333")
			.param("creditCard.cookieToken", "TEST").flashAttr("request", domainRequest).param("save", "");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Requirement 4.3 An actor who is authenticated as a user must be able to:
	 * Request a service for one of the rendezvouses that he or she's created. He or she must specify a valid credit card in every request for a service. Optionally, he or she can provide some comments in the request.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Alejandro
	 */
	@Test
	public void saveRequestNegative() throws Exception {
		Integer rendezvousId;
		Integer serviceId;
		Request request;
		DomainService dmService;

		rendezvousId = super.getEntityId("Rendezvous4");
		serviceId = super.getEntityId("DomainService3");
		super.authenticate("user1");
		dmService = this.serviceService.findOne(serviceId);

		request = new Request();
		request.setService(dmService);
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/request/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("moment", "01/01/1990 00:00").param("comment", "New request").param("rendezvousId", rendezvousId.toString())
					.param("creditCard", "").flashAttr("request", request).param("save", "")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("request/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("request.params.error")));

		super.unauthenticate();
	}

}
