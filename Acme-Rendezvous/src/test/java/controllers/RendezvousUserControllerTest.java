
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
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.user.RendezvousUserController;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class RendezvousUserControllerTest extends AbstractTest {

	private MockMvc						mockMvc;

	@InjectMocks
	@Autowired
	private RendezvousUserController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private RendezvousService			service;

	@Mock
	@Autowired
	private ConfigurationService		configurationService;


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
	 * Test the created list of Rendezvouses of the principal when anyone is logged. Must return 302 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listCreatedRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/rendezvous/user/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * Test the created list of Rendezvouses of the loggedUser in the system. Must return 200 code.
	 * The list must contains 5 elements corresponding to the first page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listRendezvousUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/rendezvous/user/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/user/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("page", Matchers.is(0))).andExpect(MockMvcResultMatchers.model().attribute("pageNum", Matchers.is(2))).andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.is(false)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasItems(Matchers.anyOf(Matchers.hasProperty("adultOnly", Matchers.is(false)), Matchers.hasProperty("adultOnly", Matchers.is(true))))));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * The user is owner of the rendezvous in final mode and can be displayed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int rendezvousId = super.getEntityId("Rendezvous8");
		request = MockMvcRequestBuilders.get("/rendezvous/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvous", Matchers.allOf(Matchers.hasProperty("id", Matchers.is(rendezvousId)), Matchers.hasProperty("finalMode", Matchers.is(false)))))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/user/edit.do"))).andExpect(MockMvcResultMatchers.model().attribute("adult", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(7))).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));
		super.unauthenticate();
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 302 code.
	 * The rendezvouses is final and must not be ldisplayed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editNotFinalRendezvousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int rendezvousId = super.getEntityId("Rendezvous1");
		request = MockMvcRequestBuilders.get("/rendezvous/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test edit a Rendezvouses in the system. Must return 302 code.
	 * The user is is not the owner of the rendezvouses and must not be displayed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editNotCreatedRendezvousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user2");
		final int rendezvousId = super.getEntityId("Rendezvous8");
		request = MockMvcRequestBuilders.get("/rendezvous/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Rendezvouses in the system. Must return 200 code.
	 * The user is owner of the rendezvous in final mode and can be displayed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void createRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		request = MockMvcRequestBuilders.get("/rendezvous/user/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvous", Matchers.hasProperty("id", Matchers.is(0)))).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("rendezvous/user/edit.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("adult", Matchers.is(true))).andExpect(MockMvcResultMatchers.model().attribute("rendezvouses", Matchers.hasSize(7)))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));
		super.unauthenticate();
	}

	/**
	 * Test create a Rendezvouses in the system. Must return 302 code.
	 * There is anyone logged and must not be displayed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void createRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		request = MockMvcRequestBuilders.get("/rendezvous/user/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

	}

	/**
	 * Test save a Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousPositive() throws Exception {
		super.authenticate("user1");

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/redezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/04/2019 00:00").param("pictureUrl", "")
					.param("gpsCoordinates", "123.12,123.12").param("similars", "[]").param("finalMode", "false").param("adultOnly", "false").sessionAttr("rendezvous", new Rendezvous()).param("save", "save"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.forwardedUrl("redirect:list.do"));

		super.unauthenticate();
	}

	/**
	 * Test create a Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		request = MockMvcRequestBuilders.post("/redezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/04/2019 00:00").param("pictureUrl", "")
			.param("gpsCoordinates", "123.12,123.12").param("similars", "[]").param("finalMode", "false").param("adultOnly", "false").param("save", "save").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.commit.error")));

	}

	/**
	 * Test create a Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user2");

		request = MockMvcRequestBuilders.post("/redezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/02/2018 00:00").param("pictureUrl", "")
			.param("gpsCoordinates", "123.12,123.12").param("similars", "[]").param("finalMode", "false").param("adultOnly", "false").param("save", "save").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.future.error")));

		super.unauthenticate();
	}

	/**
	 * Test create a Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousNotAdultNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user8");

		request = MockMvcRequestBuilders.post("/redezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/02/2019 00:00").param("pictureUrl", "")
			.param("gpsCoordinates", "123.12,123.12").param("similars", "[]").param("finalMode", "false").param("adultOnly", "false").param("save", "save").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.adult.error")));

		super.unauthenticate();
	}

	/**
	 * Test create a Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousBlankNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user8");

		request = MockMvcRequestBuilders.post("/redezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "").param("description", "").param("moment", "").param("pictureUrl", "").param("gpsCoordinates", "")
			.param("similars", "").param("finalMode", "false").param("adultOnly", "false").param("save", "save").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.params.error"))).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "description")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "moment"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "gpsCoordinates")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "similars"));

		super.unauthenticate();
	}

	/**
	 * Test create a Rendezvouses in the system. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int rendezvousId;
		final Rendezvous rendezvous;

		rendezvousId = super.getEntityId("Rendezvous8");
		rendezvous = this.service.findOne(rendezvousId);
		request = MockMvcRequestBuilders.post("/redezvous/user/edit.do?delete=delete").requestAttr("rendezvous", rendezvous);

		this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.forwardedUrl("redirect:list.do"));

		super.unauthenticate();
	}
	/**
	 * Test create a Rendezvouses in the system. Must return 302 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deleteRendezvousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int rendezvousId;
		final Rendezvous rendezvous;

		rendezvousId = super.getEntityId("Rendezvous8");
		rendezvous = this.service.findOne(rendezvousId);
		request = MockMvcRequestBuilders.post("/redezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).sessionAttr("rendezvous", rendezvous).param("delete", "delete");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

	}
}
