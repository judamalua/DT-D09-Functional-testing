
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
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.admin.AnnouncementAdminController;
import domain.Announcement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class AnnouncementAdminControllerTest extends AbstractTest {

	private MockMvc						mockMvc;

	@InjectMocks
	@Autowired
	private AnnouncementAdminController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private AnnouncementService			service;

	@Mock
	@Autowired
	private ConfigurationService		configurationService;

	@Mock
	@Autowired
	private RendezvousService			rendezvousService;


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

	// Removing announcement tests ---------------------------------------------------------------------------------

	/**
	 * This method tests that admins can remove announcements regarding functional requirement 17.1. An actor who is authenticated as a administrator must be able to
	 * remove an announcement that he or she thinks is inappropriate. Must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void testRemoveAnnouncementAsAdmin() throws Exception {
		final MockHttpServletRequestBuilder request;
		Integer announcementId;
		int rendezvousId;
		Announcement announcement;

		super.authenticate("Admin1");

		announcementId = super.getEntityId("Announcement1");
		announcement = this.service.findOne(announcementId);
		rendezvousId = this.rendezvousService.getRendezvousByAnnouncement(announcement.getId()).getId();

		request = MockMvcRequestBuilders.get("/announcement/admin/delete.do?announcementId=" + announcementId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false&pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * This driver test several negative use cases regarding functional requirement 17.1. An actor who is authenticated as a administrator must be able to
	 * remove an announcement that he or she thinks is inappropriate. Every test is explained inside and must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverNegativeRemoveAnnouncement() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that an user cannot remove an announcement he or she did not create
				"Announcement1", "User5"
			}, {
				// This test checks that a manager cannot remove an announcement
				"Announcement1", "Manager1"
			}, {
				// This test checks that unauthenticated users cannot remove an announcement
				"Announcement1", null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNegativeRemoveAnnouncement((String) testingData[i][0], (String) testingData[i][1]);
	}

	// Ancillary methods ----------------------------------------------------------------------------------------------------------------

	protected void templateNegativeRemoveAnnouncement(final String announcementPopulateName, final String user) throws Exception {
		final MockHttpServletRequestBuilder request;
		int announcementId;

		announcementId = super.getEntityId(announcementPopulateName);

		super.authenticate(user);

		request = MockMvcRequestBuilders.get("/announcement/admin/delete.do?announcementId=" + announcementId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}

}
