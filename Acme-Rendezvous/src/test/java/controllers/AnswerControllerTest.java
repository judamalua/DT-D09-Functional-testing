
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
	 * Test the list of answers of a rendezvous. Must return 200 code.
	 * 
	 * @throws Exception
	 * @author Juanmi
	 */
	@Test
	public void listAnswersOfRendezvousPositive() throws Exception {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;
		User rendezvousCreator;
		User user2;
		Collection<Answer> user2Answers;

		//TODO Add the rest of users to the expected UserAndAnswers

		rendezvousId = super.getEntityId("Rendezvous4");
		rendezvousCreator = this.userService.findOne(super.getEntityId("User1"));
		user2 = this.userService.findOne(super.getEntityId("User2"));
		user2Answers = new ArrayList<Answer>();

		request = MockMvcRequestBuilders.get("/answer/list.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("answer/user/list")).andExpect(MockMvcResultMatchers.forwardedUrl("answer/user/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("creator", Matchers.equalTo(rendezvousCreator))).andExpect(MockMvcResultMatchers.model().attribute("usersAndAnswers", Matchers.hasEntry(user2, user2Answers)))
			.andExpect(MockMvcResultMatchers.model().attribute("anonymous", Matchers.is(true)));
	}
}
