
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

import services.AnnouncementService;
import services.ConfigurationService;
import utilities.AbstractTest;
import controllers.user.AnnouncementUserController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class AnnouncementUserControllerTest extends AbstractTest {

	private MockMvc						mockMvc;

	@InjectMocks
	@Autowired
	private AnnouncementUserController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private AnnouncementService			service;

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

	// Creating announcement tests ---------------------------------------------------------------------------------

	/**
	 * This method tests that users can create announcements to rendezvouses they created, regarding functional requirement 16.3: An actor who
	 * is authenticated as a user must be able to create an announcement regarding one of the rendezvouses that he or she's created previously.
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void testCreateAnnouncement() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		super.authenticate("User1");

		rendezvousId = super.getEntityId("Rendezvous1");

		request = MockMvcRequestBuilders.get("/announcement/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("announcement/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("announcement/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvousId", Matchers.is(rendezvousId)));

		super.unauthenticate();
	}

	/**
	 * This driver test several negative use cases regarding functional requirement 16.3. An actor who is authenticated as a user must be able to create an
	 * announcement regarding one of the rendezvouses that he or she's created previously.
	 * Every test is explained inside and must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverNegativeCreateAnnouncement() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that an user cannot create an announcement to a rendezvous he or she did not create
				"Rendezvous1", "User2"
			}, {
				// This test checks that a manager cannot create an announcement to a rendezvous
				"Rendezvous1", "Manager1"
			}, {
				// This test checks that an admin cannot create an announcement to a rendezvous
				"Rendezvous1", "Admin1"
			}, {
				// This test checks that unauthenticated users cannot create an announcement to a rendezvous
				"Rendezvous1", null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNegativeCreateAnnouncement((String) testingData[i][0], (String) testingData[i][1]);
	}

	// Listing announcements of RSVPed rendezvouses tests ------------------------------------------------------------------------------------------

	/**
	 * This method tests that users can list announcements of rendezvouses they RSVPed, regarding functional requirement 16.5: An actor who is authenticated
	 * as a user must be able to display a stream of announcements that have been posted to the rendezvouses that he or she's RSVPd.
	 * The announcements must be listed chronologically in descending order.
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void testListAnnouncementsOfRsvpedRendezvouses() throws Exception {
		final MockHttpServletRequestBuilder request;

		super.authenticate("User1");

		request = MockMvcRequestBuilders.get("/announcement/user/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("announcement/list")).andExpect(MockMvcResultMatchers.forwardedUrl("announcement/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("announcement/user/list.do"))).andExpect(MockMvcResultMatchers.model().attribute("associatedRendezvouses", Matchers.hasSize(9)))
			.andExpect(MockMvcResultMatchers.model().attribute("announcements", Matchers.hasSize(9)));

		super.unauthenticate();
	}

	/**
	 * This driver test several negative use cases regarding functional requirement 16.5: An actor who is authenticated as a user must be able to display
	 * a stream of announcements that have been posted to the rendezvouses that he or she's RSVPd. The announcements must be listed
	 * chronologically in descending order.
	 * Every test is explained inside and must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverListAnnouncementsOfRsvpedRendezvouses() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that an admin cannot list the announcements of the rendezvouses he or she RSVPed
				"Admin1"
			}, {
				// This test checks that a manager cannot list the announcements of the rendezvouses he or she RSVPed
				"Manager1"
			}, {
				// This test checks that unauthenticated users cannot list the announcements of the rendezvouses he or she RSVPed
				null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListAnnouncementsOfRsvpedRendezvouses((String) testingData[i][0]);
	}

	// Listing created announcements tests ------------------------------------------------------------------------------------------

	/**
	 * This method tests that users can list the announcements they created.
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void testListCreatedAnnouncements() throws Exception {
		final MockHttpServletRequestBuilder request;

		super.authenticate("User1");

		request = MockMvcRequestBuilders.get("/announcement/user/list-created.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("announcement/list")).andExpect(MockMvcResultMatchers.forwardedUrl("announcement/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("announcement/user/list-created.do"))).andExpect(MockMvcResultMatchers.model().attribute("associatedRendezvouses", Matchers.hasSize(9)))
			.andExpect(MockMvcResultMatchers.model().attribute("announcements", Matchers.hasSize(9)));

		super.unauthenticate();
	}

	/**
	 * This driver test several negative use cases regarding list of created announcements from users.
	 * Every test is explained inside and must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverListCreatedAnnouncements() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that an admin cannot list his or her created announcements
				"Admin1"
			}, {
				// This test checks that a manager cannot list his or her created announcements
				"Manager1"
			}, {
				// This test checks that unauthenticated users cannot list his or her created announcements
				null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListCreatedAnnouncements((String) testingData[i][0]);
	}

	// Saving announcement tests -------------------------------------------------------------------------------------------------------

	/**
	 * This method tests that users can save announcements to rendezvouses they created, regarding functional requirement 16.3: An actor who
	 * is authenticated as a user must be able to create an announcement regarding one of the rendezvouses that he or she's created previously.
	 * Must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void saveAnnouncementAsUser() throws Exception {
		int rendezvousId;

		super.authenticate("User1");

		rendezvousId = super.getEntityId("Rendezvous5");

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/announcement/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("title", "Title test").param("description", "Description test").param("rendezvousId", Integer.toString(rendezvousId))
					.param("save", "")).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * This driver test several negative use cases regarding functional requirement 16.3: An actor who
	 * is authenticated as a user must be able to create an announcement regarding one of the rendezvouses that he or she's created previously.
	 * Every test is explained inside and must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverNegativeSaveAnnouncementWithAllParameters() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that an user cannot create an announcement to a Rendezvous he or she did not create
				"Rendezvous5", "User2", "Title test", "Description test"
			}, {
				// This test checks that an admin cannot create an announcement to a Rendezvous
				"Rendezvous5", "Admin1", "Title test", "Description test"
			}, {
				// This test checks that a manager cannot create an announcement to a Rendezvous
				"Rendezvous5", "Manager1", "Title test", "Description test"
			}, {
				// This test checks that unauthenticated users cannot create an announcement to a Rendezvous
				"Rendezvous5", null, "Title test", "Description test"
			}, {
				// This test checks that an user cannot create an announcement to a Rendezvous he or she created without title
				"Rendezvous5", "User1", "", "Description test"
			}, {
				// This test checks that an user cannot create an announcement to a Rendezvous he or she created without description
				"Rendezvous5", "User1", "Title test", ""
			}, {
				// This test checks that an user cannot create an announcement to a Rendezvous he or she created without title and description
				"Rendezvous5", "User1", "", ""
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNegativeSaveAnnouncementWithAllParameters((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3]);
	}

	// Ancillary methods ----------------------------------------------------------------------------------------------------------------

	protected void templateNegativeCreateAnnouncement(final String rendezvousPopulateName, final String user) throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId(rendezvousPopulateName);

		super.authenticate(user);

		request = MockMvcRequestBuilders.get("/announcement/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5&rendezvousId=" + rendezvousId));

		super.unauthenticate();
	}

	protected void templateListAnnouncementsOfRsvpedRendezvouses(final String user) throws Exception {
		final MockHttpServletRequestBuilder request;

		super.authenticate(user);

		request = MockMvcRequestBuilders.get("/announcement/user/list.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}

	protected void templateListCreatedAnnouncements(final String user) throws Exception {
		final MockHttpServletRequestBuilder request;

		super.authenticate(user);

		request = MockMvcRequestBuilders.get("/announcement/user/list-created.do");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}

	protected void templateNegativeSaveAnnouncementWithAllParameters(final String rendezvousPopulateName, final String user, final String titleText, final String descriptionText) throws Exception {
		int rendezvousId;

		super.authenticate(user);

		rendezvousId = super.getEntityId(rendezvousPopulateName);

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/announcement/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("title", titleText).param("description", descriptionText).param("rendezvousId", Integer.toString(rendezvousId))
					.param("save", "").param("message", "announcement.commit.error")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("announcement/edit"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("announcement/edit"));

		super.unauthenticate();
	}
}
