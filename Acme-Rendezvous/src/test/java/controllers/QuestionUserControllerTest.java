
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
import services.QuestionService;
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.user.QuestionUserController;
import domain.Question;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class QuestionUserControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@InjectMocks
	@Autowired
	private QuestionUserController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private QuestionService			service;

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
	 * Test list of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listQuestionNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous1");

		request = MockMvcRequestBuilders.get("/question/user/list.do?rendezvousId" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Test list of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 302 code and redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void listNotOwnQuestionNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;
		super.authenticate("user2");
		rendezvousId = super.getEntityId("Rendezvous1");

		request = MockMvcRequestBuilders.get("/question/user/list.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

		super.unauthenticate();
	}

	/**
	 * Test list of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * The list must contains 5 elements corresponding to the first page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listQuestionsUserLogedPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;
		Rendezvous rendezvous;

		super.authenticate("user1");

		rendezvousId = super.getEntityId("Rendezvous1");

		rendezvous = this.rendezvousService.findOne(rendezvousId);

		request = MockMvcRequestBuilders.get("/question/user/list.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("question/list")).andExpect(MockMvcResultMatchers.forwardedUrl("question/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvousName", Matchers.is(rendezvous.getName()))).andExpect(MockMvcResultMatchers.model().attribute("rendezvousId", Matchers.is(rendezvousId)))
			.andExpect(MockMvcResultMatchers.model().attribute("questions", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.model().attribute("rendezvousIsInFinalMode", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvousIsDeleted", Matchers.is(false)));

		super.unauthenticate();
	}

	/**
	 * Test edit of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 200 code.
	 * The logged user get the view of a own question.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void editQuestionPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int questionId, rendezvousId;

		questionId = super.getEntityId("Question6");
		rendezvousId = super.getEntityId("Rendezvous8");

		request = MockMvcRequestBuilders.get("/question/user/edit.do?questionId=" + questionId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("question/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("question/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("question", Matchers.allOf(Matchers.hasProperty("id", Matchers.is(questionId))))).andExpect(MockMvcResultMatchers.model().attribute("rendezvousId", Matchers.is(rendezvousId)))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test edit of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 302 code.
	 * The question is not final and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editNotExistingQuestionNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int questionId = 1;
		request = MockMvcRequestBuilders.get("/question/user/edit.do?questionId=" + questionId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test edit of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 302 code.
	 * The user is is not the owner of the questions and must redirect error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void editNotCreatedQuestionNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user2");
		final int questionId = super.getEntityId("question6");
		request = MockMvcRequestBuilders.get("/question/user/edit.do?questionId=" + questionId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test create of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 200 code.
	 * The user is owner of the question in final mode and can be displayed.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void createQuestionPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");

		super.authenticate("user1");

		request = MockMvcRequestBuilders.get("/question/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("question/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("question/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("question", Matchers.allOf(Matchers.hasProperty("id", Matchers.is(0))))).andExpect(MockMvcResultMatchers.model().attribute("rendezvousId", Matchers.is(rendezvousId)))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.isEmptyOrNullString()));

		super.unauthenticate();
	}

	/**
	 * Test create of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * 
	 * Must return 302 code.
	 * There is anyone logged and must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void createquestionNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");
		request = MockMvcRequestBuilders.get("/question/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));
	}

	/**
	 * Test save of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 302 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void savequestionPositive() throws Exception {
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");
		super.authenticate("user1");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/question/user/edit.do?rendezvousId=" + rendezvousId).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("text", "New question").sessionAttr("question", new Question()).param("save", ""))
			.andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:list.do?rendezvousId=" + rendezvousId)).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5&rendezvousId=" + rendezvousId));

		super.unauthenticate();
	}

	/**
	 * Test save of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * 
	 * Must return 200 code.
	 * There is no one logged and must redirect to error page
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveQuestionNotLoggedNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");

		request = MockMvcRequestBuilders.post("/question/user/edit.do?rendezvousId=" + rendezvousId).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("text", "New question").sessionAttr("question", new Question()).param("save", "");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}

	/**
	 * Test save of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 200 code.
	 * The moment must be in future, then the system must return the past error code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void saveQuestionNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		rendezvousId = super.getEntityId("Rendezvous8");
		super.authenticate("user1");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/question/user/edit.do?rendezvousId=" + rendezvousId).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("text", "").sessionAttr("question", new Question()).param("save", ""))
			.andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("question/edit")).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("question.params.error")));

		super.unauthenticate();
	}

	/**
	 * Test save of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 200 code.
	 * The question has blank required parameters then they must be redirect to the edit page with these fields with errors.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void savequestionBlankNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user8");

		request = MockMvcRequestBuilders.post("/question/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "").param("description", "").param("moment", "").param("pictureUrl", "").param("gpsCoordinates", "")
			.param("similars", "").param("finalMode", "false").param("adultOnly", "false").param("save", "").sessionAttr("question", new question());

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("question/edit")).andExpect(MockMvcResultMatchers.forwardedUrl("question/edit"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.is("question.params.error"))).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("question", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("question", "description")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("question", "moment"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("question", "gpsCoordinates")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("question", "similars"));

		super.unauthenticate();
	}

	/**
	 * Test delete of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 200 code.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deletequestionPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		super.authenticate("user1");
		final int questionId;
		final question question;

		questionId = super.getEntityId("question8");
		question = this.service.findOne(questionId);
		request = MockMvcRequestBuilders.post("/question/user/edit.do").param("delete", "").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("question", question);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:list.do")).andExpect(MockMvcResultMatchers.redirectedUrl("list.do?pagesize=5"));

		super.unauthenticate();
	}
	/**
	 * Test delete of Questions of an question, regarding functional requirement 21.1, an actor who
	 * is authenticated as a user must be able to Manage the questions that are associated
	 * with a question that he or she’s created previously.
	 * 
	 * Must return 302 code.
	 * No one is logged and the system must redirect to error page.
	 * 
	 * @throws Exception
	 * @author MJ
	 */
	@Test
	public void deletequestionNegative() throws Exception {
		final MockHttpServletRequestBuilder request;
		final int questionId;
		final question question;

		questionId = super.getEntityId("question8");
		question = this.service.findOne(questionId);
		request = MockMvcRequestBuilders.post("/question/user/edit.do").contentType(MediaType.APPLICATION_FORM_URLENCODED).flashAttr("question", question).param("delete", "");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.redirectedUrl("/misc/403?pagesize=5"));

	}
}
