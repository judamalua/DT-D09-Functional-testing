
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import services.RendezvousService;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------------
	public CommentController() {
		super();
	}

	//List -----------------------------------------------------------
	@RequestMapping(value = "/listFromRendezvous", method = RequestMethod.GET)
	public ModelAndView listFromRendezvous(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Comment> comments;
		Rendezvous rendezvous;
		String requestURI;

		rendezvous = this.rendezvousService.findOneForReplies(rendezvousId);
		comments = rendezvous.getComments();
		requestURI = "comment/list.do";

		result = new ModelAndView("comment/list");
		result.addObject("comments", comments);
		result.addObject("requestURI", requestURI);

		return result;
	}

	@RequestMapping(value = "/listFromComment", method = RequestMethod.GET)
	public ModelAndView listFromComment(@RequestParam final int commentId) {
		ModelAndView result;
		Collection<Comment> replies;
		Comment comment;
		String requestURI;
		Boolean userHasRVSPdRendezvous = false;
		Rendezvous rendezvous;

		comment = this.commentService.findOne(commentId);
		replies = comment.getComments();
		requestURI = "comment/list.do";

		result = new ModelAndView("comment/list");
		result.addObject("comments", replies);
		result.addObject("requestURI", requestURI);

		try {
			User user;
			Comment fatherComment;

			user = (User) this.actorService.findActorByPrincipal();

			rendezvous = this.rendezvousService.getRendezvousByCommentary(commentId);

			fatherComment = comment;
			while (rendezvous == null) {
				fatherComment = this.commentService.getFatherCommentFromReply(comment);
				rendezvous = this.rendezvousService.getRendezvousByCommentary(fatherComment.getId());
			}

			userHasRVSPdRendezvous = rendezvous.getUsers().contains(user);

			result.addObject("userHasRVSPdRendezvous", userHasRVSPdRendezvous);
		} catch (final Throwable oops) {

		}

		return result;
	}
	//Display -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commentId) {
		final ModelAndView result;
		Collection<Comment> replies;
		Comment comment;
		User user;

		comment = this.commentService.findOne(commentId);
		replies = comment.getComments();

		user = this.commentService.getUserFromComment(comment);
		result = new ModelAndView("comment/display");
		result.addObject("comment", comment);
		result.addObject("user", user);
		result.addObject("comments", replies);

		return result;
	}
}
