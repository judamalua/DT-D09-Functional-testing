
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
	 * Test the created list of Rendezvouses of the principal when anyone is logged, regarding functional requirement.
	 * 
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listCreatedRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;

		request = MockMvcRequestBuilders.get("/rendezvous/user/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Test the created list of Rendezvouses of the logged user in the system. Must return 200 code.
	 * 
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
	 * Test edit view of Rendezvouses in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * Must return 200 code.
	 * The logged user get the view of a own Rendezvous.
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
	 * Test the edit view of Rendezvouses in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed..
	 * 
	 * Must return 302 code.
	 * The rendezvous is not final and must redirect to error page.
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
	 * Test the edit view of Rendezvouses in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
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
		request = MockMvcRequestBuilders.get("/rendezvous/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test the public list of Rendezvouses in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * Must return 200 code.
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
	 * Test create a Rendezvouses in the system,, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * 
	 * Must return 302 code.
	 * There is anyone logged and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void createRendezvousNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		request = MockMvcRequestBuilders.get("/rendezvous/user/create.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Test save correct Rendezvous in the system,, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * Must return 302 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousPositive() throws Exception {
		int userId;
		super.authenticate("user1");
		userId = super.getEntityId("User1");

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/rendezvous/user/edit.do")

				.contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/04/2019 00:00").param("pictureUrl", "").param("gpsCoordinates", "123.12,123.12")
					.param("finalMode", "false").param("adultOnly", "false").sessionAttr("rendezvous", new Rendezvous()).param("save", ""))

			.andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5&userId=" + userId));

		super.unauthenticate();
	}

	/**
	 * Test save Rendezvous in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed.
	 * 
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
		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/04/2019 00:00")
			.param("pictureUrl", "").param("gpsCoordinates", "123.12,123.12").param("similars", "[]").param("finalMode", "false").param("adultOnly", "false").param("save", "").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}

	/**
	 * Test save Rendezvous with past moment in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed..
	 * 
	 * Must return 200 code.
	 * The moment must be in future, then the system must return the past error code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user2");

		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/02/2018 00:00")
			.param("pictureUrl", "").param("gpsCoordinates", "123.12,123.12").param("finalMode", "false").param("adultOnly", "false").param("save", "").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.future.error")));

		super.unauthenticate();
	}

	/**
	 * Test save adult Rendezvous in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed..
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
		super.authenticate("user8");

		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "New Rendezvous").param("description", "New Description").param("moment", "09/02/2019 00:00")
			.param("pictureUrl", "").param("gpsCoordinates", "123.12,123.12").param("finalMode", "false").param("adultOnly", "true").param("save", "").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.adult.error")));

		super.unauthenticate();
	}

	/**
	 * Test save blank parameters Rendezvous in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed..
	 * 
	 * Must return 200 code.
	 * The rendezvous has blank required parameters then they must be redirect to the edit page with these fields with errors.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveRendezvousBlankNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user8");

		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "").param("description", "").param("moment", "").param("pictureUrl", "").param("gpsCoordinates", "")
			.param("similars", "").param("finalMode", "false").param("adultOnly", "false").param("save", "").sessionAttr("rendezvous", new Rendezvous());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("rendezvous/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("rendezvous.params.error"))).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "description")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "moment"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "gpsCoordinates")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rendezvous", "similars"));

		super.unauthenticate();
	}

	/**
	 * Test delete Rendezvous in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed..
	 * 
	 * Must return 200 code.
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
		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").param("delete", "").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("rendezvous", rendezvous);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5"));

		super.unauthenticate();
	}
	/**
	 * Test delete Rendezvouses in the system, regarding functional requirement 5.3
	 * An actor who is authenticated as a user must be able to Update or delete the
	 * rendezvouses that he or she's created. Deletion
	 * is virtual, that is: the information is not removed from the database, but the
	 * rendezvous cannot be updated. Deleted rendezvouses are flagged as such when they are displayed..
	 * 
	 * Must return 302 code.
	 * No one is logged and the system must redirect to error page.
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
		request = MockMvcRequestBuilders.post("/rendezvous/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("rendezvous", rendezvous).param("delete", "");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}
}
