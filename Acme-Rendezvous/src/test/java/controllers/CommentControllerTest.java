
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

import services.CommentService;
import services.ConfigurationService;
import utilities.AbstractTest;
import controllers.user.CommentUserController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class CommentControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	//Controller under test ----------------------
	@InjectMocks
	@Autowired
	private CommentUserController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private CommentService			service;

	@Mock
	@Autowired
	private ConfigurationService	configurationService;


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

	@Test
	public void testCreateCommentToRendezvousPositive() throws Throwable {
		final MockHttpServletRequestBuilder mockHttpServletRequestBuilder;
		int rendezvousId;

		super.authenticate("user1");

		rendezvousId = super.getEntityId("Rendezvous4"); //Table games

		mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/comment/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(mockHttpServletRequestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("comment/edit"));
	}
}
