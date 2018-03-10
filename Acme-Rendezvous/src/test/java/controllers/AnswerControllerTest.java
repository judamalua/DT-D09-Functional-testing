
package controllers;

import java.util.ArrayList;
import java.util.Collection;

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

import services.AnswerService;
import services.ConfigurationService;
import services.UserService;
import utilities.AbstractTest;
import domain.Answer;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class AnswerControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private AnswerController		controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private AnswerService			service;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;

	@Mock
	@Autowired
	private UserService				userService;


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
	 * This method tests the list of answers of a rendezvous, regarding functional requirement 20.1: an actor who is not authenticated must be able to display
	 * information about the users who have RSVPd a rendezvous, which, in turn, must show their answers to the questions that the creator has registered.
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void listAnswersOfRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;
		User rendezvousCreator;
		User user2, user3, user4, user7;
		Collection<Answer> user2Answers, user3Answers, user4Answers, user7Answers;

		rendezvousId = super.getEntityId("Rendezvous4");
		rendezvousCreator = this.userService.findOne(super.getEntityId("User1"));
		user2 = this.userService.findOne(super.getEntityId("User2"));
		user3 = this.userService.findOne(super.getEntityId("User3"));
		user4 = this.userService.findOne(super.getEntityId("User4"));
		user7 = this.userService.findOne(super.getEntityId("User7"));

		user2Answers = new ArrayList<Answer>();
		user3Answers = new ArrayList<Answer>();
		user4Answers = new ArrayList<Answer>();
		user7Answers = new ArrayList<Answer>();

		request = MockMvcRequestBuilders.get("/answer/list.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("answer/user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("answer/user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("creator", Matchers.equalTo(rendezvousCreator))).andExpect(MockMvcResultMatchers.model().attribute("usersAndAnswers", Matchers.hasEntry(user2, user2Answers)))
			.andExpect(MockMvcResultMatchers.model().attribute("usersAndAnswers", Matchers.hasEntry(user3, user3Answers))).andExpect(MockMvcResultMatchers.model().attribute("usersAndAnswers", Matchers.hasEntry(user4, user4Answers)))
			.andExpect(MockMvcResultMatchers.model().attribute("usersAndAnswers", Matchers.hasEntry(user7, user7Answers)));
	}

	/**
	 * This driver checks that answer list of a deleted, draft mode or null rendezvous cannot be acceded, regarding functional requirement 20.1: an actor who is not authenticated
	 * must be able to display information about the users who have RSVPd a rendezvous, which, in turn, must show their answers to the questions that
	 * the creator has registered. Every test inside this method must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverListAnswers() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that answer list of a draft mode rendezvous cannot be acceded
				"Rendezvous8"
			}, {
				// This test checks that answer list of a draft mode rendezvous cannot be acceded
				"Rendezvous9"
			}, {
				// This test checks that answer list of a null rendezvous cannot be acceded
				null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListAnswers((String) testingData[i][0]);
	}

	protected void templateListAnswers(final String rendezvousPopulateName) throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		if (rendezvousPopulateName != null)
			rendezvousId = super.getEntityId(rendezvousPopulateName);
		else
			rendezvousId = 0;

		request = MockMvcRequestBuilders.get("/answer/list.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
}
