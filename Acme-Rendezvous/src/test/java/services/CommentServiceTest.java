
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	// The SUT --------------------------------------------------------------
	@Autowired
	private CommentService		commentService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezvousService	rendezvousService;


	//******************************************Positive Methods*******************************************************************

	/**
	 * 5.6 An actor who is authenticated as a user must be able to: Comment on the rendezvouses that he or she has RSVPd.
	 * 
	 * This test checks that an user can write comments in his rcvps rendezvouses
	 * 
	 * @author Luis
	 * */
	@Test
	public void testUserCanWriteCommentsInHisRcvpsRendezvouses() {
		super.authenticate("User1");
		final Comment comment;
		final Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(this.getEntityId("Rendezvous4"));

		comment = this.createStandardComment();
		//		rendezvous.getComments().add(comment);
		//		user.getCreatedRendezvouses().add(rendezvous);

		this.commentService.save(comment, rendezvous);
		this.commentService.flush();
		this.rendezvousService.flush();

		super.unauthenticate();

	}
	/**
	 * 6.1 An actor who is authenticated as an administrator must be able to:Remove a comment that he or she thinks is inappropriate.
	 * 
	 * This test checks that an admin can delete comments
	 * 
	 * @author Luis
	 * */
	@Test
	public void testAdminCanDeleteComments() {
		super.authenticate("Admin1");
		final Comment comment;

		comment = this.commentService.findOne(super.getEntityId("Comment1"));

		this.commentService.delete(comment);
		this.commentService.flush();
		this.rendezvousService.flush();

		super.unauthenticate();

	}

	/**
	 * 5.6 An actor who is authenticated as a user must be able to: Comment on the rendezvouses that he or she has RSVPd.
	 * 
	 * This test checks that an user can`t write comments in a not rsvp rendezvous
	 * 
	 * @author Luis
	 * */
	@Test(expected = IllegalArgumentException.class)
	public void testUserCantWriteCommentsInANotRcvpRendezvous() {
		super.authenticate("User2");
		final Comment comment;
		final Rendezvous rendezvous;

		rendezvous = this.rendezvousService.findOne(this.getEntityId("Rendezvous8"));

		comment = this.createStandardComment();
		//		rendezvous.getComments().add(comment);
		//		user.getCreatedRendezvouses().add(rendezvous);

		this.commentService.save(comment, rendezvous);
		this.commentService.flush();
		this.rendezvousService.flush();

		super.unauthenticate();
	}
	//******************************************Negative Methods*******************************************************************

	/**
	 * 
	 * 
	 * This test checks that an actor who is not authenticated can´t write any comments
	 * 
	 * @author Luis
	 * */
	@Test(expected = IllegalArgumentException.class)
	public void testNoAuthenticatedCantWriteComments() {
		super.authenticate(null);
		Comment comment;

		comment = this.createStandardComment();

		this.commentService.save(comment);
		this.commentService.flush();

		super.unauthenticate();

	}

	//Private Methods

	private Comment createStandardComment() {
		Comment comment;

		comment = this.commentService.create();

		comment.setPictureUrl("https://image.freepik.com/iconos-gratis/comentario-ios-7-simbolo-interfaz_318-33559.jpg");
		comment.setText("That is a example of a standard text than can appear in a comment");
		comment.setUser((User) this.actorService.findActorByPrincipal());

		return comment;

	}

}
