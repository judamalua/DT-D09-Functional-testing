
package controllers;

import java.util.Collection;

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
import org.springframework.util.Assert;

import services.CommentService;
import services.ConfigurationService;
import services.RendezvousService;
import utilities.AbstractTest;
import controllers.admin.CommentAdministratorController;
import domain.Comment;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class CommentAdministratorControllerTest extends AbstractTest {

	private MockMvc							mockMvc;

	//Controller under test ----------------------
	@InjectMocks
	@Autowired
	private CommentAdministratorController	controller;

	//Service under test ------------------------
	@Mock
	@Autowired
	private CommentService					service;

	@Mock
	@Autowired
	private ConfigurationService			configurationService;

	@Mock
	@Autowired
	private RendezvousService				rendezvousService;


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
	 * Test the delete comment function for an Administrator. The comment is linked to a Rendezvous. When deleted,
	 * the system must return to the detailed rendezvous view. Functional requirement 6.1:
	 * An actor who is authenticated as an administrator must be able to:
	 * - Remove a comment that he or she thinks is inappropriate
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDeleteCommentInRendezvousByAdminPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int commentId, rendezvousId;
		Collection<Comment> comments;
		Comment comment;
		Rendezvous rendezvous;

		super.authenticate("admin");

		commentId = super.getEntityId("Comment8");
		rendezvousId = super.getEntityId("Rendezvous4");
		comment = this.service.findOne(commentId);

		request = MockMvcRequestBuilders.get("/comment/admin/delete.do?commentId=" + commentId + "&rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"))
			.andExpect(MockMvcResultMatchers.forwardedUrl(null));
		;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		comments = rendezvous.getComments();

		Assert.isTrue(!comments.contains(comment));

		super.unauthenticate();
	}

	/**
	 * Test the delete comment function, logged as an User. The comment is linked to a Rendezvous. When deleted,
	 * the system must return to the detailed rendezvous view.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDeleteCommentInRendezvousNotByAdminNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int commentId, rendezvousId;
		Collection<Comment> comments;
		Comment comment;
		Rendezvous rendezvous;

		super.authenticate("user1");

		commentId = super.getEntityId("Comment8");
		rendezvousId = super.getEntityId("Rendezvous4");
		comment = this.service.findOne(commentId);

		request = MockMvcRequestBuilders.get("/comment/admin/delete.do?commentId=" + commentId + "&rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
		;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		comments = rendezvous.getComments();

		Assert.isTrue(comments.contains(comment));

		super.authenticate("manager1");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
		;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		comments = rendezvous.getComments();

		Assert.isTrue(comments.contains(comment));

		super.unauthenticate();

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));
		;

		rendezvous = this.rendezvousService.findOne(rendezvousId);
		comments = rendezvous.getComments();

		Assert.isTrue(comments.contains(comment));
	}

	/**
	 * Test the delete comment function for an Administrator. The comment doesn't exists. The system
	 * must return to the 403 view.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDeleteCommentThatDoesntExistInRendezvousByAdminNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int commentId, rendezvousId;

		super.authenticate("admin");

		commentId = super.getEntityId("Announcement1"); //Instead of a comment, this is an announcement.
		rendezvousId = super.getEntityId("Rendezvous4");

		request = MockMvcRequestBuilders.get("/comment/admin/delete.do?commentId=" + commentId + "&rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/misc/403")).andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test the delete comment function for an Administrator. The comment exists but we don't pass any rendezvousId param.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDeleteCommentWithoutRendezvousByAdminNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int commentId;

		super.authenticate("admin");

		commentId = super.getEntityId("Comment8");

		request = MockMvcRequestBuilders.get("/comment/admin/delete.do?commentId=" + commentId + "&rendezvousId=");

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(200)).andExpect(MockMvcResultMatchers.view().name("misc/panic")).andExpect(MockMvcResultMatchers.forwardedUrl("misc/panic"));

		super.unauthenticate();
	}
	/**
	 * Test the delete comment function for an Administrator. The comment exists but it isn't from that Rendezvous.
	 * The comment should be deleted, but the system must redirect to the detailed view of the rendezvous passed as a param.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDeleteCommentInBadRendezvousByAdminNegative() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int commentId, rendezvousId;

		super.authenticate("admin");

		commentId = super.getEntityId("Comment8"); //Instead of a comment, this is an announcement.
		rendezvousId = super.getEntityId("Rendezvous5");

		request = MockMvcRequestBuilders.get("/comment/admin/delete.do?commentId=" + commentId + "&rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"))
			.andExpect(MockMvcResultMatchers.forwardedUrl(null));

		super.unauthenticate();
	}

	/**
	 * Test the delete comment function for an Administrator. The comment is a reply of other comment. When deleted,
	 * the system must return to the detailed rendezvous view.
	 * 
	 * @throws Throwable
	 * @author Antonio
	 */
	@Test
	public void testDeleteCommentRepliedInRendezvousByAdminPositive() throws Throwable {
		final MockHttpServletRequestBuilder request;
		int commentId, rendezvousId, fatherCommentId;
		Collection<Comment> comments;
		Comment comment, fatherComment;

		super.authenticate("admin");

		fatherCommentId = super.getEntityId("Comment9");
		fatherComment = this.service.findOne(fatherCommentId);
		commentId = super.getEntityId("Comment10");
		rendezvousId = super.getEntityId("Rendezvous5");
		comment = this.service.findOne(commentId);
		comments = fatherComment.getComments();

		Assert.isTrue(comments.contains(comment));

		request = MockMvcRequestBuilders.get("/comment/admin/delete.do?commentId=" + commentId + "&rendezvousId=" + rendezvousId);

		this.mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(302)).andExpect(MockMvcResultMatchers.view().name("redirect:/rendezvous/detailed-rendezvous.do?rendezvousId=" + rendezvousId + "&anonymous=false"))
			.andExpect(MockMvcResultMatchers.forwardedUrl(null));
		;

		fatherComment = this.service.findOne(fatherCommentId);

		comments = fatherComment.getComments();

		Assert.isTrue(!comments.contains(comment));

		super.unauthenticate();
	}

}
