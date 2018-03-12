
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

import services.CommentService;
import services.ConfigurationService;
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.user.CommentUserController;
import domain.Comment;

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
	private RendezvousService		rendezvousService;

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

	//-----------------------CREATE COMMENT TESTS------------------------------

	/**
	 * Test the create comment view for a user who has RSVPd the Rendezvous. Must return 200 code.
	 * Functional requirement:
	 * 5.6) Comment on the rendezvouses that he or she has RSVPd.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testCreateCommentToRendezvousPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		super.authenticate("user1"); //The user has RSVPd the Rendezvous

		rendezvousId = super.getEntityId("Rendezvous4"); //Table games

		request = MockMvcRequestBuilders.get("/comment/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("comment/edit")).andExpect(MockMvcResultMatchers.model().attribute("comment", Matchers.hasProperty("id", Matchers.is(0))))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvous", Matchers.hasProperty("id", Matchers.is(rendezvousId))))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("comment/user/edit.do?rendezvousId=" + rendezvousId)));

	}

	/**
	 * Test the create comment POST for a user who has RSVPd the Rendezvous. Must return 200 code.
	 * Functional requirement:
	 * 5.6) Comment on the rendezvouses that he or she has RSVPd.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testSaveCommentToRendezvousPositive() throws Throwable {
		int rendezvousId;

		super.authenticate("user1");
		rendezvousId = super.getEntityId("Rendezvous4"); //Table games

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/comment/user/edit.do?rendezvousId=" + rendezvousId).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("text", "Comment Test text").param("pictureUrl", "http://majoumo.com/images/picture/picture2.jpg")
					.param("moment", "03/03/2018 00:15").sessionAttr("comment", new Comment()).param("save", "")).andExpect(MockMvcResultMatchers.status().is(302))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"));

		super.unauthenticate();
	}

	/**
	 * Test the create comment view for a user who hasn't RSVPd the Rendezvous. Must return 302 code.
	 * Functional requirement:
	 * 5.6) Comment on the rendezvouses that he or she has RSVPd.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testCreateCommentToRendezvousRSVPdNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int rendezvousId;

		super.authenticate("user5"); //The user hsn't RSVPd the Rendezvous

		rendezvousId = super.getEntityId("Rendezvous4"); //Table games

		request = MockMvcRequestBuilders.get("/comment/user/create.do?rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

	}

	//-----------------------REPLY COMMENT TESTS------------------------------

	/**
	 * Test the reply comment view for a user who has RSVPd the Rendezvous. Must return 200 code.
	 * Information requirement;
	 * 19) In addition to writing a comment from scratch, a user may reply to a comment.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testReplyCommentPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int repliedCommentId;
		int rendezvousId;

		super.authenticate("user1"); //The user has RSVPd the Rendezvous

		repliedCommentId = super.getEntityId("Comment8"); //Replied comment
		rendezvousId = super.getEntityId("Rendezvous4"); //Table games

		request = MockMvcRequestBuilders.get("/comment/user/reply.do?commentId=" + repliedCommentId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("comment/edit")).andExpect(MockMvcResultMatchers.model().attribute("comment", Matchers.hasProperty("id", Matchers.is(0))))
			.andExpect(MockMvcResultMatchers.model().attribute("replied", Matchers.hasProperty("id", Matchers.is(repliedCommentId))))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("comment/user/reply.do?repliedId=" + repliedCommentId)))
			.andExpect(MockMvcResultMatchers.model().attribute("rendezvous", Matchers.hasProperty("id", Matchers.is(rendezvousId))));

	}

	/**
	 * Test the reply comment view for a user who hasn't RSVPd the Rendezvous. Must return 302 code.
	 * Information requirement;
	 * 19) In addition to writing a comment from scratch, a user may reply to a comment.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testReplyCommentNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int repliedCommentId;

		super.authenticate("user5"); //The user hasn't RSVPd the Rendezvous

		repliedCommentId = super.getEntityId("Comment8"); //Replied comment

		request = MockMvcRequestBuilders.get("/comment/user/reply.do?commentId=" + repliedCommentId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

	}
}
