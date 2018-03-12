
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
import org.springframework.util.Assert;

import services.AnswerService;
import services.ConfigurationService;
import services.QuestionService;
import utilities.AbstractTest;
import controllers.user.AnswerUserController;
import domain.Question;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class AnswerUserControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private AnswerUserController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private AnswerService			service;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;

	@Mock
	@Autowired
	private QuestionService			questionService;

	@Mock
	@Autowired
	private AnswerService			answerService;


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

	// Listing questions to answer tests ---------------------------------------------------------------------------------

	/**
	 * This method tests that when a user tries to RSVP a final mode Rendezvous with questions, he or she will be redirected to a view where will be asked to
	 * answer the questions of the Rendezvous, regarding functional requirement 21.2: An actor who is authenticated as a user must be able
	 * to answer the questions that are associated with a rendezvous that he or she's RSVPing now. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void listOfQuestionsToAnswerOfRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId, question7Id, question8Id, question9Id;
		Question question7, question8, question9;

		rendezvousId = super.getEntityId("Rendezvous7");

		super.authenticate("User3");

		question7Id = super.getEntityId("Question7");
		question7 = this.questionService.findOne(question7Id);

		question8Id = super.getEntityId("Question8");
		question8 = this.questionService.findOne(question8Id);

		question9Id = super.getEntityId("Question9");
		question9 = this.questionService.findOne(question9Id);

		request = MockMvcRequestBuilders.get("/answer/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("answer/user/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("answer/user/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("questions", Matchers.hasItems(question7, question8, question9)));

		super.unauthenticate();
	}

	/**
	 * This driver test several negative use cases regarding functional requirement 21.2: An actor who is authenticated as a user must be able
	 * to answer the questions that are associated with a rendezvous that he or she's RSVPing now. Every test is explained inside and must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverNegativeListQuestionsToAnswer() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that when a user tries to answer to the question of a rendezvous with a past moment, he or she is not allowed to do so
				"Rendezvous1", "User2"
			}, {
				// This test checks that when a user tries to answer to the question of a draft mode rendezvous, he or she is not allowed to do so
				"Rendezvous8", "User2"
			}, {
				// This test checks that when a user tries to answer to the question of a deleted rendezvous, he or she is not allowed to do so
				"Rendezvous9", "User2"
			}, {
				// This test checks that when a user tries to answer to the question of a rendezvous that has already joined, he or she is not allowed to do so
				"Rendezvous5", "User2"
			}, {
				//This test checks that when a user tries to answer to the question of a rendezvous that has created, he or she is not allowed to do so
				"Rendezvous5", "User1"
			}, {
				//This test checks that unauthenticated users cannot answer to the questions of a rendezvous
				"Rendezvous7", null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNegativeListQuestionsToAnswer((String) testingData[i][0], (String) testingData[i][1]);
	}

	/**
	 * This method tests that when a user tries to RSVP a final mode Rendezvous without questions, he or she will be redirected to the detailed view of
	 * the Rendezvous, regarding functional requirement 21.2: An actor who is authenticated as a user must be able
	 * to answer the questions that are associated with a rendezvous that he or she's RSVPing now. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void emptyListOfQuestionsToAnswerOfRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous6");

		super.authenticate("User2");

		request = MockMvcRequestBuilders.get("/answer/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"));

		super.unauthenticate();
	}

	// Deleting answers when leaving a rendezvous tests ---------------------------------------------------------------------------------

	/**
	 * This method tests that when a user can leave a Rendezvous he or she has RSVPed before, and the answers associated to the questions of the rendezvous are deleted,
	 * regarding functional requirement 21.2: An actor who is authenticated as a user must be able to answer the questions that are associated with
	 * a rendezvous that he or she's RSVPing now. Must return 302 code since it is a redirection.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void deleteAnswersWhenLeavingRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous7");

		super.authenticate("User2");

		// Checking that answers are stored in the database
		Assert.notNull(this.answerService.findOne(super.getEntityId("Answer1")));
		Assert.notNull(this.answerService.findOne(super.getEntityId("Answer2")));
		Assert.notNull(this.answerService.findOne(super.getEntityId("Answer3")));

		request = MockMvcRequestBuilders.get("/answer/user/delete.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"));

		// Checking that answers were properly deleted
		Assert.isNull(this.answerService.findOne(super.getEntityId("Answer1")));
		Assert.isNull(this.answerService.findOne(super.getEntityId("Answer2")));
		Assert.isNull(this.answerService.findOne(super.getEntityId("Answer3")));

		super.unauthenticate();
	}

	//TODO Make save tests
	/**
	 * This driver test several negative use cases regarding functional requirement 21. An actor who is authenticated as a user must be able to answer the
	 * questions that are associated with a rendezvous that he or she's RSVPing now (We are checking that when an user leaves a rendezvous, his/her answers
	 * associated to rendezvous questions are deleted). Every test is explained inside and must return 302 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void driverNegativeDeleteAnswersWhenLeavingRendezvous() throws Exception {
		final Object testingData[][] = {
			{
				// This test checks that an user cannot delete answers of a draft mode rendezvous
				"Rendezvous8", "User2"
			}, {
				// This test checks that an user cannot delete answers of a deleted rendezvous
				"Rendezvous9", "User2"
			}, {
				// This test checks that an user cannot delete answers of a finished rendezvous
				"Rendezvous1", "User2"
			}, {
				// This test checks that an user cannot delete answers of a rendezvous he or she did not RSVP
				"Rendezvous7", "User3"
			}, {
				// This test checks that an user cannot delete answers of a rendezvous he or she created it
				"Rendezvous7", "User1"
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNegativeDeleteAnswersWhenLeavingRendezvous((String) testingData[i][0], (String) testingData[i][1]);
	}

	// Ancillary methods ----------------------------------------------------------------------------------------------------------------

	protected void templateNegativeListQuestionsToAnswer(final String rendezvousPopulateName, final String user) throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId(rendezvousPopulateName);

		super.authenticate(user);

		request = MockMvcRequestBuilders.get("/answer/user/edit.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403"));

		super.unauthenticate();
	}

	protected void templateNegativeDeleteAnswersWhenLeavingRendezvous(final String rendezvousPopulateName, final String user) throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId(rendezvousPopulateName);

		super.authenticate(user);

		request = MockMvcRequestBuilders.get("/answer/user/delete.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403"));

		super.unauthenticate();
	}
}
